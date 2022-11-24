package com.springcloud.test.alibabaconsumer.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring security安全配置
 *
 * @author 肖龙威
 * @date 2022/11/24 14:53
 */
//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .usernameParameter("userName")
                .passwordParameter("password")
                .loginProcessingUrl("/login");

    }
}
