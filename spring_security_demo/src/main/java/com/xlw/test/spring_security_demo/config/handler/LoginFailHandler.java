package com.xlw.test.spring_security_demo.config.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.xlw.test.spring_security_demo.entity.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:
 * @Title: LoginFailHandler
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.config.handler
 * @Date 2024/9/20 23:17
 */
public class LoginFailHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        Result<String> result = Result.fail(authException.getMessage());
        PrintWriter writer = response.getWriter();
        JSONConfig config = new JSONConfig();
        config.setIgnoreNullValue(false);
        config.setDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN);
        try {
            writer.println(JSONUtil.toJsonStr(result, config));
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
