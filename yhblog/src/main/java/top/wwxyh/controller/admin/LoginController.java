package top.wwxyh.controller.admin;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.wwxyh.common.dto.LoginDto;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.LoginLog;
import top.wwxyh.entity.User;
import top.wwxyh.service.LoginLogService;
import top.wwxyh.service.UserService;
import top.wwxyh.util.JwtUtils;
import top.wwxyh.util.UserAgentUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 登录
 * @Author: wwx
 * @Date: 2021/3/30 20:54
 */
@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    LoginLogService loginLogService;

    @Autowired
    UserAgentUtils userAgentUtils;

    /**
     * @Author wwx
     * @Description  后台登录
     * @Date 2021/4/25 0:37
     * @Param [loginDto, response, request]
     * @return top.wwxyh.common.lang.Result
     **/
    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request){
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
//        Assert.notNull(user,"用户不存在");
        if (user == null){
            return Result.fail("用户不存在");
        }

        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }

        String jwt = JwtUtils.generateToken(user.getUsername());
        //为了后续的jwt的延期，所以把jwt放在  header上
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        //记录登录日志信息
        LoginLog log = handleLog(request, true, "登录成功");
        log.setUsername(user.getUsername());
        loginLogService.save(log);

        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }

    /**
     * @Author wwx
     * @Description  前台页面登录
     * @Date 2021/4/25 0:39
     * @Param [loginDto, response, request]
     * @return top.wwxyh.common.lang.Result
     **/
    @CrossOrigin
    @PostMapping("/loginViews")
    public Result loginViews(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request){
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
//        Assert.notNull(user,"用户不存在");
        if (user == null){
            return Result.fail("用户不存在");
        }

        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }

        String jwt = JwtUtils.generateToken(user.getUsername());

        //记录登录日志信息
        LoginLog log = handleLog(request, true, "前台登录成功");
        log.setUsername(user.getUsername());
        loginLogService.save(log);

        return Result.succ("登录成功",jwt);
    }

    @CrossOrigin
    @PostMapping("/login1")
    public Result login1(@RequestBody @Validated LoginDto loginDto){
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        try {
            //构造登录令牌usernamePasswordToken
            //加密密码
            password = new Md5Hash(password).toString();  //1.密码  2.盐  3.加密次数
            UsernamePasswordToken upToken = new UsernamePasswordToken(username, password);
            //获取subject
            Subject subject = SecurityUtils.getSubject();
            //调用login方法，进入realm完成认证
            subject.login(upToken);

            //登录成功后签发token
            String jwt = JwtUtils.generateToken(username);
            //获取sessionId
            //String sessionId = (String) subject.getSession().getId();

            User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
            HashMap<String, Object> map = new HashMap<>();
            map.put("jwt",jwt);
            Map<Object, Object> userInfo = MapUtil.builder()
                    .put("id", user.getId())
                    .put("username", user.getUsername())
                    .put("avatar", user.getAvatar())
                    .put("email", user.getEmail())
                    .map();
            map.put("userInfo",userInfo);

            return Result.succ(map);
        } catch (Exception e){
            return Result.fail("登录失败");
        }
    }

    /**
     * @Author wwx
     * @Description  设置LoginLog对象属性
     * @Date 2021/4/6 22:46
     * @Param [request, status, description]
     * @return top.wwxyh.entity.LoginLog
     **/
    private LoginLog handleLog(HttpServletRequest request, boolean status, String description) {
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
