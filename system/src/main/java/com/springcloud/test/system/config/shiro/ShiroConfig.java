package com.springcloud.test.system.config.shiro;

import cn.hutool.core.codec.Base64;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 肖龙威
 * @date 2022/09/08 14:40
 */
@Configuration
public class ShiroConfig {


    //创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm验证数据
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        Cookie cookie = new SimpleCookie();
        cookie.setMaxAge(2592000); //30天
        cookie.setHttpOnly(true);
        cookie.setName("rememberMeCookie");
        rememberMeManager.setCookie(cookie);
        return rememberMeManager;
    }

    @Bean
    public Realm realm() {
        CustomerRealm realm = new CustomerRealm();
        //设置密码配置
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置使用MD5加密算法
        credentialsMatcher.setHashAlgorithmName("md5");
        //散列次数
        credentialsMatcher.setHashIterations(1024);
        realm.setCredentialsMatcher(credentialsMatcher);
        return realm;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Cookie cookie = new SimpleCookie();
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        cookie.setName("SessionId");
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationInterval(1000*60*15);
        sessionManager.setGlobalSessionTimeout(1000*60*30);
        return sessionManager;
    }

    //ShiroFilter过滤所有请求
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给ShiroFilter配置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //自定义过滤器
        Map<String, Filter> customFilter = new HashMap();
        //customFilter.put("cauth",authFilter());
        shiroFilterFactoryBean.setFilters(customFilter);
        //设置认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/users/login");
        //配置系统受限资源
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        /*
        认证顺序是从上往下执行。
         */
        //linkedHashMap.put("/logout", "logout");//在这儿配置登出地址，不需要专门去写控制器。
        linkedHashMap.put("/static/**", "anon");
        //开启注册页面不需要权限
        linkedHashMap.put("/register", "anon");
        //验证码判断
        linkedHashMap.put("/comparecode", "anon");
        linkedHashMap.put("/websocket", "anon");//必须开启。
        linkedHashMap.put("/css/**", "anon");//不需要验证
        linkedHashMap.put("/js/**", "anon");//不需要验证
        //配置错误页面
        linkedHashMap.put("error", "anon");//不需要验证
        linkedHashMap.put("/img/**", "anon");//不需要验证
        linkedHashMap.put("/layui/**", "anon");//不需要验证
        linkedHashMap.put("/video/**", "anon");//不需要验证
        linkedHashMap.put("/bower_components/**", "anon");//不需要验证
        linkedHashMap.put("/plugins/**", "anon");//不需要验证
        linkedHashMap.put("/dist/**", "anon");//不需要验证
        linkedHashMap.put("/doc.html*/**", "anon");
        linkedHashMap.put("/actuator/health", "anon");
        //linkedHashMap.put("/**", "cauth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(linkedHashMap);
        //设置未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/users/login");
        return shiroFilterFactoryBean;
    }

}
