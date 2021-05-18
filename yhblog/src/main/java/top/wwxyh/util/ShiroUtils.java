package top.wwxyh.util;

import top.wwxyh.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtils {

    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
