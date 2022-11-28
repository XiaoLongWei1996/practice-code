package com.springcloud.test.alibabaconsumer.config.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 肖龙威
 * @date 2022/11/28 15:56
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        response.setStatus(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("没有访问权限");
    }
}
