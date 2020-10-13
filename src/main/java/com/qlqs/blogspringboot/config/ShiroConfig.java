package com.qlqs.blogspringboot.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.qlqs.blogspringboot.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("/","anon");
        map.put("/login","anon");
        map.put("/logout","user");
        map.put("/user/freshcomment","anon");
        map.put("/user/insertNewUserAccount","anon");
        map.put("/user/**","user");
        map.put("/**/**","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        shiroFilterFactoryBean.setLoginUrl("/page/login");
        shiroFilterFactoryBean.setSuccessUrl("/user/background");

        return shiroFilterFactoryBean;
    }
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") Realm realm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setSessionManager(getDefaultWebSessionManager());
        securityManager.setRememberMeManager(getcookieRememberMeManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //配置sessionManager
        sessionManager.setGlobalSessionTimeout(30*60*1000);
        return sessionManager;
    }
    @Bean
    public CookieRememberMeManager getcookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie("rememberme");
        simpleCookie.setMaxAge(30*24*60*60);
        cookieRememberMeManager.setCookie(simpleCookie);
        return cookieRememberMeManager;
    }
    @Bean("userRealm")
    public Realm getRealm(){
        UserRealm realm = new UserRealm();
        //设置hashed凭证匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置md5加密
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        realm.setCredentialsMatcher(hashedCredentialsMatcher);
        return realm;
    }
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
