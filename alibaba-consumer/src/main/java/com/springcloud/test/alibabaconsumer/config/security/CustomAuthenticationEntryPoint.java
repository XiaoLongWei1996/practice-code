package com.springcloud.test.alibabaconsumer.config.security;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义身份验证入口点
 *
 * @author 肖龙威
 * @date 2022/11/25 15:19
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String,Object> resultData = new HashMap<>();
        resultData.put("code","401");
        resultData.put("msg", "未登陆");
        out.write(JSONObject.toJSONString(resultData));
        out.flush();
        out.close();
    }
}
