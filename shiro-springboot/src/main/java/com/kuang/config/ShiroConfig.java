package com.kuang.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/11/6 14:59
 */
@Configuration
public class ShiroConfig {
    // ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        bean.setSecurityManager(securityManager);

        // 添加shrio的内置过滤器
        /*
        * anon:无需认证就可以访问
        * authc: 必须认证才可以访问
        * user: 必须拥有 记住我 才能使用
        * perms: 拥有某个资源的权限才能使用
        * role: 拥有某个角色权限才能使用
        * */

        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();

        /* 必须先授权再拦截，否则授权会失效：*/
        /* 1 授权操作: 所有访问/user/add的资源必须拥有user:add权限；可以再Realm中设置权限 */
        filterMap.put("/user/add", "perms[user:add]");
        filterMap.put("/user/update", "perms[user:update]");

        /* 2 拦截操作 */
        /*filterMap.put("/user/add", "authc");
        filterMap.put("/user/update", "authc");
        也可以改成以下代码，对user/下所有资源使用authc权限拦截 */
        filterMap.put("/user/*", "authc");

        // 如果没有登录，就去登录页面。这里设置登录页面
        bean.setLoginUrl("/toLogin");
        // 如果没有授权，就跳转到授权页面
        bean.setUnauthorizedUrl("/noauth");


        bean.setFilterChainDefinitionMap(filterMap);

        return bean;
    }

    /* 为securityManager自定义名称securityManager，否则使用方法名作为Bean的name属性值就是getDefaultWebSecurityManager */
    @Bean(name = "securityManager")
    // DefaultWebSecurityManager
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Autowired UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联userRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    // 创建 realm 对象，需要自定义realm类:定义认证规则
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    // 整合ShiroDialect:用来整合shiro thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

}
