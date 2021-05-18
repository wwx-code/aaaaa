package top.wwxyh.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.entity.ExceptionLog;
import top.wwxyh.service.ExceptionLogService;
import top.wwxyh.util.AopUtils;
import top.wwxyh.util.JacksonUtils;
import top.wwxyh.util.StringUtils;
import top.wwxyh.util.UserAgentUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
public class ExceptionLogAspect {

    @Autowired
    ExceptionLogService exceptionLogService;

    @Autowired
    UserAgentUtils userAgentUtils;

    //配置切入点
    @Pointcut("execution(* top.wwxyh.controller..*.*(..))")
    public void logPointcut(){}

    //异常抛出增强，相当于ThrowsAdvice.
    @AfterThrowing(value = "logPointcut()",throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint,Exception e){
        ExceptionLog log = handleLog(joinPoint, e);
        exceptionLogService.save(log);
    }

    //获取HttpServletRequest请求对象，并设置ExceptionLog对象属性
    private ExceptionLog handleLog(JoinPoint joinpoint,Exception e){
        //在Service层获取获取request信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //从request中取出对应的数据存入exception_log
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String description = getDescriptionFromAnnotations(joinpoint);
        String error = StringUtils.getStackTrace(e);
        //当存在反向代理软件时就不能获取到客户端的真实IP地址了，后续优化
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        ExceptionLog log = new ExceptionLog(uri, method, description, error, ip, userAgent);

//        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> requestParams = AopUtils.getRequestParams(joinpoint);
        log.setParam(StringUtils.substring(JacksonUtils.writeValueAsString(requestParams), 0, 2000));

        //解析客户端操作系统、浏览器
        Map<String, String> userAgentMap = userAgentUtils.parseOsAndBrowser(userAgent);
        String os = userAgentMap.get("os");
        String browser = userAgentMap.get("browser");
        log.setOs(os);
        log.setBrowser(browser);

        return log;
    }

    //获取操作描述
    private String getDescriptionFromAnnotations(JoinPoint joinPoint){
        String description = "";
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OperationLogger operationLogger = method.getAnnotation(OperationLogger.class);
        if (operationLogger != null) {
            description = operationLogger.value();
            return description;
        }
        VisitLogger visitLogger = method.getAnnotation(VisitLogger.class);
        if (visitLogger != null) {
            description = visitLogger.behavior();
            return description;
        }
        return description;
    }
}
