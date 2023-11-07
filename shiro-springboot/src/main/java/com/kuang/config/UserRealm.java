package com.kuang.config;

import com.kuang.pojo.User;
import com.kuang.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.sasl.AuthorizeCallback;
import java.security.Security;
import java.util.Set;

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

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 从数据库中获取User对象，获取perms值（用户权限值）
        Subject subject = SecurityUtils.getSubject();
        // 获取认证方法中存入的principal值：当前User对象
        User currentUser = (User) subject.getPrincipal();

        // 设置当前的用户权限：如果在数据库中获取的perms=null是会报错500
        if(currentUser.getPerms() != null) {
            info.addStringPermission(currentUser.getPerms());
        }

        return info;
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
        // 将user对象放在SimpleAuthenticationInfo的principle属性中，在授权的方法中就可以获取User对象
        return new SimpleAuthenticationInfo(user, user.getPwd(), "");
    }
}
