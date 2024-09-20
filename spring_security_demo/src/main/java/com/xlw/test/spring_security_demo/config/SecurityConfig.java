package com.xlw.test.spring_security_demo.config;

import com.xlw.test.spring_security_demo.config.filter.TokenAuthFilter;
import com.xlw.test.spring_security_demo.config.handler.LoginFailHandler;
import com.xlw.test.spring_security_demo.service.impl.AuthServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.annotation.Resource;

/**
 * @description:
 * @Title: SecurityConfig
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.config
 * @Date 2024/9/20 17:16
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService authService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置权限管理，白名单
        http.authorizeHttpRequests()
                .antMatchers(Cache.PATH_WHITELIST)
                .permitAll()
                .anyRequest()
                .authenticated() //别的请求都需要认证
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginFailHandler());
        //添加认证过滤器
        http.addFilterBefore(new TokenAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        //禁用csrf和cors
        http.cors().and().csrf().disable();
        //禁用原始的表单登录
        http.formLogin().disable();
        //禁用原始的登出
        http.logout().disable();
        //禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置验证器，加密等
     *
     * @param auth 认证
     * @throws Exception 例外
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(multiAuthenticationProvider())
                .userDetailsService(authService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationProvider multiAuthenticationProvider() {
        return new MultiAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }
}
