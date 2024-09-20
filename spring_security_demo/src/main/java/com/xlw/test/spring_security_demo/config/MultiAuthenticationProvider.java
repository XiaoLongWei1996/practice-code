package com.xlw.test.spring_security_demo.config;

import cn.hutool.core.util.StrUtil;
import com.xlw.test.spring_security_demo.service.impl.AuthServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description: 多功能认证校验器
 * @Title: MultiAuthenticationProvider
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.config
 * @Date 2024/9/20 22:22
 */
public class MultiAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new AuthException("获取认证信息失败");
        }
        if (!(authentication instanceof MultiAuthenticationToken)) {
            throw new AuthException("认证对象不正确");
        }
        MultiAuthenticationToken authToken = (MultiAuthenticationToken) authentication;
        //获取真实的用户数据
        UserDetails userDetails = userDetailsService.loadUserByUsername(authToken.getPrincipal());
        if (userDetails == null) {
            throw new AuthException("用户名不存在");
        }
        if (authToken.getLoginType() == 1) {
            //密码登录
            if (!passwordEncoder.matches(authToken.getCredentials(), userDetails.getPassword())) {
                throw new AuthException("用户名或者密码不正确");
            }
        } else if (authToken.getLoginType() == 2) {
            //短信登录
            String code = Cache.SMS_CACHE.get(authToken.getPrincipal());
            if (StrUtil.isBlank(code)) {
                throw new AuthException("短信验证码已过期");
            }

            if (!Objects.equals(code, authToken.getSmsCode())) {
                throw new AuthException("短信验证码不正确");
            }
        }
        //设置详情
        authToken.setDetails(userDetails);
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MultiAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
