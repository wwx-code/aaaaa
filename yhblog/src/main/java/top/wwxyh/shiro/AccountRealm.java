package top.wwxyh.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.User;
import top.wwxyh.service.UserService;
import top.wwxyh.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * AccountRealm是shiro进行登录或者权限校验的逻辑所在
 */
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    public void setName(String name){
        super.setName("AccountRealm");
    }

    //让realm支持jwt的凭证校验
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //权限校验，授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //1.获取安全数据 username/用户id
        AccountProfile user = (AccountProfile) principalCollection.getPrimaryPrincipal();
        //2.根据id或者名称查询用户
        String role = user.getRole();
        List<String> perms = new ArrayList<>();
        perms.add(role);
        /*perms.add("user:update");
        //3.查询用户的角色和权限信息
        List<String> roles = new ArrayList<>();
        roles.add("roles1");
        roles.add("roles2");*/
        //4.构造返回
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //设置权限集合
        info.addStringPermissions(perms);
//        info.addRoles(roles);
        return info;

    }

    //登录认证校验，身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;

        //获取用户名
        String username = jwtUtils.getTokenBody((String) jwtToken.getPrincipal()).getSubject();

        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));

        if (user == null){
//            throw new UnknownAccountException("账户不存在");
            throw new ShiroException("账户不存在");
        }

        AccountProfile profile = new AccountProfile();
        BeanUtils.copyProperties(user,profile);

        //密码认证，shiro全权处理
        //profile：安全数据  credentials：密码   realmName：当前realm域名称
        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }
}
