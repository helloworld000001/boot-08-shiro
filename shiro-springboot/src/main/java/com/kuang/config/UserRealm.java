package com.kuang.config;

import com.kuang.pojo.User;
import com.kuang.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.sasl.AuthorizeCallback;
import java.security.Security;

/**
 * @auther 陈彤琳
 * @Description $ 自定义realm
 * 2023/11/6 15:01
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService  userService;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权 doGetAuthorizationInfo");
        return null;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证 doGetAuthenticationInfo");
        

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        //连接真实的数据库
        User user = userService.queryUserByName(userToken.getUsername());

        if(user == null){
            //没有这个user,抛出异常：UnknownAccountException
            return null;
        }

        // 密码认证：由shiro完成
        return new SimpleAuthenticationInfo("", user.getPwd(), "");
    }
}
