package top.wwxyh.shiro;

import cn.hutool.json.JSONUtil;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.UsernamePasswordToken;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.LoginLog;
import top.wwxyh.service.LoginLogService;
import top.wwxyh.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import top.wwxyh.util.UserAgentUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    LoginLogService loginLogService;

    @Autowired
    UserAgentUtils userAgentUtils;

//    ThreadLocal<String> currentUsername = new ThreadLocal<>();

    //实现登录，需要生成自定义支持的JwtToken
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //从请求头部获取jwt
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            return null;
        }

        return new JwtToken(jwt);
    }


    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //后台管理路径外的请求直接跳过
        if (!request.getRequestURI().startsWith("/admin") || request.getRequestURI().startsWith("/admin/login")) {
            return true;
        }
        String jwt = request.getHeader("Authorization");
        if (jwtUtils.judgeTokenIsExist(jwt)){
            Claims claim = jwtUtils.getTokenBody(jwt);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
//                throw new ExpiredCredentialsException("token已失效，请重新登录");
                throw new ShiroException("token已失效，请重新登录");
            }
            //执行登录
            return executeLogin(servletRequest, servletResponse);
        }else {
            return false;
        }
    }

    //登录异常时候进入的方法，直接把异常信息封装然后抛出
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //获取异常原因
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        //统一结果封装
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        LoginLog log = handleLog(request1, false, throwable.getMessage());
        loginLogService.save(log);

        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * @Author wwx
     * @Description  设置LoginLog对象属性
     * @Date 2021/4/6 22:46
     * @Param [request, status, description]
     * @return top.wwxyh.entity.LoginLog
     **/
    private LoginLog handleLog(HttpServletRequest request, boolean status, String description) {
//        String username = currentUsername.get();
//        String username = jwtUtils.getTokenBody((String) jwtToken.getPrincipal()).getSubject();
//        currentUsername.remove();
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        //解析客户端操作系统、浏览器
        Map<String, String> userAgentMap = userAgentUtils.parseOsAndBrowser(userAgent);
        String os = userAgentMap.get("os");
        String browser = userAgentMap.get("browser");

        LoginLog log = new LoginLog(ip, status, description, userAgent, os, browser);
        return log;
    }
}
