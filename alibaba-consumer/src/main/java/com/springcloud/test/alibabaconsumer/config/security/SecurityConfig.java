package com.springcloud.test.alibabaconsumer.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security安全配置
 *
 * @author 肖龙威
 * @date 2022/11/24 14:53
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyLoginSuccessHandler loginSuccessHandler;

    private final MyLogoutSuccessHandler logoutSuccessHandler;

    private final MyAuthenticationFailureHandler loginFailureHandler;

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    private final MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置登录
        http.formLogin()
                .usernameParameter("userName")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler);
        //配置登出
        http.logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler);
        //配置授权
        http.authorizeHttpRequests()
                .antMatchers("/login", "/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(myAccessDeniedHandler);
        //关闭session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //添加自定义过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //禁用跨域和csrf
        http.cors().and().csrf().disable();
    }
}
