package com.xlw.test.spring_security_demo.config;

import com.xlw.test.spring_security_demo.config.filter.TokenAuthFilter;
import com.xlw.test.spring_security_demo.config.handler.LoginFailHandler;
import com.xlw.test.spring_security_demo.config.handler.NotAccessHandler;
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
        //权限认证
        //http.authorizeHttpRequests().antMatchers("/sys/test").hasAnyAuthority("sys:test", "ROLE_ADMIN");

        //配置权限管理，白名单
        http.authorizeHttpRequests()
                .antMatchers(Cache.PATH_WHITELIST)
                .permitAll()
                .anyRequest()
                .authenticated() //别的请求都需要认证
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginFailHandler())  //设置认证失败处理器
                .accessDeniedHandler(new NotAccessHandler()); //设置无权访问处理器
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

    /**
     * 没有这个启动项目会报错
     * 认证管理器
     *
     * @return {@link AuthenticationManager }
     * @throws Exception 例外
     */
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
        auth.authenticationProvider(multiAuthenticationProvider())  //注入多功能认证器
                .userDetailsService(authService)    //注入用户详情服务
                .passwordEncoder(passwordEncoder());  //注入密码加密器
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
