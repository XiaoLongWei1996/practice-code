package com.xlw.test.spring_security_demo.config.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.xlw.test.spring_security_demo.entity.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 肖龙威
 * @date 2022/11/28 15:56
 */
public class NotAccessHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        try {
            JSONConfig config = new JSONConfig();
            config.setIgnoreNullValue(false);
            config.setDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN);
            Result<String> result = new Result(HttpServletResponse.SC_FORBIDDEN, null, "无权访问");
            writer.println(JSONUtil.toJsonStr(result, config));
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
