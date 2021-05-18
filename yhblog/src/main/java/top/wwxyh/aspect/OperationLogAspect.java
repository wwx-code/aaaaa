package top.wwxyh.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.entity.OperationLog;
import top.wwxyh.service.OperationLogService;
import top.wwxyh.util.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Aspect
public class OperationLogAspect {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserAgentUtils userAgentUtils;

    @Autowired
    OperationLogService operationLogService;

    //获取当前线程变量，用于计算操作请求耗时times
    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    //配置切入点，指定带有OperationLogger注解的方法执行时记录
    @Pointcut("@annotation(operationLogger)")
    public void logPointcut(OperationLogger operationLogger){
    }

    /**
     * 配置环绕通知
     * @param joinPoint
     * @param operationLogger
     * @return
     */
    @Around("logPointcut(operationLogger)")
    public Object logAround(ProceedingJoinPoint joinPoint,OperationLogger operationLogger) throws Throwable {
        //考虑到多人访问的情况采用线程
        //设置当前线程的线程局部变量的值
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        int times = (int) (System.currentTimeMillis() - currentTime.get());
        //将当前线程局部变量的值删除，目的是为了减少内存的占用
        currentTime.remove();

        /*
        StopWatch stopWatch = new StopWatch(); //第二种方法，使用Spring提供的stopWatch来统计一段代码的执行时间
        stopWatch.start();  //开始
        Object result = joinPoint.proceed();
        stopWatch.stop();  //结束
        int times = (int) stopWatch.getTotalTimeMillis();  //获取执行时间
        */

        OperationLog operationLog = handleLog(joinPoint, operationLogger, times);
        operationLogService.save(operationLog);
        return result;
    }

    /**
     * 获取HttpServletRequest请求对象，并设置OperationLog对象属性
     * @param joinPoint
     * @param operationLogger
     * @param times
     * @return
     */
    private OperationLog handleLog(ProceedingJoinPoint joinPoint, OperationLogger operationLogger, int times){
        //在Service层获取获取request信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //operationLogger注解必须加在需要认证的方法上（已登录的情况下访问），否则jwt会获取为空抛异常
        String username = jwtUtils.getTokenBody(request.getHeader("Authorization")).getSubject();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String description = operationLogger.value();
        //当存在反向代理软件时就不能获取到客户端的真实IP地址了，后续优化
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        //设置OperationLog对象属性
        OperationLog log = new OperationLog(username, uri, method, description, ip, times, userAgent);

        //获取请求参数
        Map<String, Object> requestParams = AopUtils.getRequestParams(joinPoint);
        log.setParam(StringUtils.substring(JacksonUtils.writeValueAsString(requestParams), 0, 2000));

        //解析客户端操作系统、浏览器
        Map<String, String> userAgentMap = userAgentUtils.parseOsAndBrowser(userAgent);
        String os = userAgentMap.get("os");
        String browser = userAgentMap.get("browser");
        log.setOs(os);
        log.setBrowser(browser);

        return log;
    }
}
