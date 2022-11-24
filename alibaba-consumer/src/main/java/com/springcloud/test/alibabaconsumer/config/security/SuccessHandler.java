package com.springcloud.test.alibabaconsumer.config.security;

import com.springcloud.test.alibabaconsumer.service.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功处理器
 * @author 肖龙威
 * @date 2022/11/24 16:49
 */
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private UsersService usersService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }
}
