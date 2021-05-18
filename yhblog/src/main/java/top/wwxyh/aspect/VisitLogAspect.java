package top.wwxyh.aspect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.common.vo.BlogDetail;
import top.wwxyh.entity.VisitLog;
import top.wwxyh.entity.Visitor;
import top.wwxyh.service.VisitLogService;
import top.wwxyh.service.VisitorService;
import top.wwxyh.util.AopUtils;
import top.wwxyh.util.JacksonUtils;
import top.wwxyh.util.StringUtils;
import top.wwxyh.util.UserAgentUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:  AOP记录访问日志
 * @Author: wwx
 * @Date: 2021/3/26 20:50
 */
@Component
@Aspect
public class VisitLogAspect {

    @Autowired
    VisitLogService visitLogService;

    @Autowired
    VisitorService visitorService;

    @Autowired
    UserAgentUtils userAgentUtils;

    //获取当前线程变量，用于计算操作请求耗时times
    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    @Pointcut("@annotation(visitLogger)")
    public void logPointcut(VisitLogger visitLogger){}

    /**
     * @Author wwx
     * @Description  配置环绕通知
     * @Date 2021/3/27 10:35
     * @Param [joinPoint, visitLogger]
     * @return java.lang.Object
     **/
    @Around("logPointcut(visitLogger)")
    public Object logAround(ProceedingJoinPoint joinPoint,VisitLogger visitLogger) throws Throwable {
        //考虑到多人访问的情况采用线程
        //设置当前线程的线程局部变量的值
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        int times = (int) (System.currentTimeMillis() - currentTime.get());
        //将当前线程局部变量的值删除，目的是为了减少内存的占用
        currentTime.remove();

        //在Service层获取获取request信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        //校验访客标识码
        String identification = checkIdentification(request);
        //记录访问日志
        VisitLog visitLog = handleLog(joinPoint, visitLogger, request, result, times, identification);
        visitLogService.save(visitLog);

        return result;
    }

    /**
     * @Author wwx
     * @Description  校验访客标识码
     * @Date 2021/3/27 11:06
     * @Param [request]
     * @return java.lang.String
     **/
    private String checkIdentification(HttpServletRequest request){
        String identification = request.getHeader("identification");
            if (identification == null){
            //第一次访问，调用save方法
            identification = saveUUID(request);
        }else{
            //查询数据库是否存在uuid
            Visitor visitor = visitorService.getOne(new QueryWrapper<Visitor>().eq("uuid", identification));
            if (visitor == null){
                //数据库不存在，签发新的uuid
                identification = saveUUID(request);
            }else {
                //存在，pv+1
                //获取当前pv
                Integer currentPv = visitor.getPv();
                //构造更新Wrapper
                UpdateWrapper<Visitor> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",visitor.getId())
                        .set("pv",currentPv+1);
                visitorService.update(updateWrapper);
            }
        }
        return identification;
    }

    /**
     * @Author wwx
     * @Description  签发UUID，并保存至数据库和Redis
     * @Date 2021/3/27 11:30
     * @Param [request]
     * @return java.lang.String
     **/
    public String saveUUID(HttpServletRequest request){
        //在Service层获取获取响应对象
        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attribute.getResponse();
        //生成UUID
        String uuid = UUID.randomUUID().toString();
        //添加访客标识码UUID至响应头
        response.addHeader("identification",uuid);
        //暴露自定义header供页面资源使用
        response.addHeader("Access-Control-Expose-Headers", "identification");
        //保存至redis

        //获取访问者基本信息
        //当存在反向代理软件时就不能获取到客户端的真实IP地址了，后续优化
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        //解析客户端操作系统、浏览器
        Map<String, String> userAgentMap = userAgentUtils.parseOsAndBrowser(userAgent);
        String os = userAgentMap.get("os");
        String browser = userAgentMap.get("browser");

        Visitor visitor = new Visitor(uuid, ip, userAgent, os, browser);
        visitorService.save(visitor);
        return uuid;
    }

    /**
     * @Author wwx
     * @Description  设置VisitLogger对象属性
     * @Date 2021/3/27 15:32
     * @Param [joinPoint, visitLogger, request, result, times, identification]
     * @return top.wwxyh.entity.VisitLog
     **/
    private VisitLog handleLog(ProceedingJoinPoint joinPoint,
                               VisitLogger visitLogger,
                               HttpServletRequest request,
                               Object result,
                               int times,
                               String identification){
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String behavior = visitLogger.behavior();
        String content = visitLogger.content();
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> requestParams = AopUtils.getRequestParams(joinPoint);
        Map<String, String> map = judgeBehavior(behavior, content, requestParams, result);
        VisitLog visitLog = new VisitLog(identification, uri, method, behavior, map.get("content"), map.get("remark"), ip, times, userAgent);
        visitLog.setParam(StringUtils.substring(JacksonUtils.writeValueAsString(requestParams), 0, 2000));

        //解析客户端操作系统、浏览器
        Map<String, String> userAgentMap = userAgentUtils.parseOsAndBrowser(userAgent);
        String os = userAgentMap.get("os");
        String browser = userAgentMap.get("browser");
        visitLog.setOs(os);
        visitLog.setBrowser(browser);

        return visitLog;
    }

    private Map<String, String> judgeBehavior(String behavior, String content, Map<String, Object> requestParams, Object result) {
        Map<String, String> map = new HashMap<>();
        String remark = "";
        if (behavior.equals("访问页面") && (content.equals("首页") || content.equals("动态"))) {
            int pageNum = (int) requestParams.get("pageNum");
            remark = "第" + pageNum + "页";
        } else if (behavior.equals("查看博客")) {
            Result res = (Result) result;
            if (res.getCode() == "200") {
                BlogDetail blog = (BlogDetail) res.getData();
                String title = blog.getTitle();
                content = title;
                remark = "文章标题：" + title;
            }
        } else if (behavior.equals("搜索博客")) {
            Result res = (Result) result;
            if (res.getCode() == "200") {
                String query = (String) requestParams.get("query");
                content = query;
                remark = "搜索内容：" + query;
            }
        } else if (behavior.equals("查看分类")) {
            String categoryName = (String) requestParams.get("categoryName");
            int pageNum = (int) requestParams.get("pageNum");
            content = categoryName;
            remark = "分类名称：" + categoryName + "，第" + pageNum + "页";
        } else if (behavior.equals("查看标签")) {
            String tagName = (String) requestParams.get("tagName");
            int pageNum = (int) requestParams.get("pageNum");
            content = tagName;
            remark = "标签名称：" + tagName + "，第" + pageNum + "页";
        } else if (behavior.equals("点击友链")) {
            String nickname = (String) requestParams.get("nickname");
            content = nickname;
            remark = "友链名称：" + nickname;
        }
        map.put("remark", remark);
        map.put("content", content);
        return map;
    }
}
