package com.springcloud.test.alibabaconsumer.config.security;

import com.alibaba.fastjson.JSONObject;
import com.springcloud.test.alibabaconsumer.entity.Users;
import com.springcloud.test.alibabaconsumer.service.UsersService;
import com.springcloud.test.alibabaconsumer.util.TokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证成功处理器
 * @author 肖龙威
 * @date 2022/11/24 16:49
 */
@Component
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private UsersService usersService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Users users = loginUser.getUsers();
        users.setUpdateDt(new Date());
        users.setLoginDt(new Date());
        users.setState(1);
        usersService.updateById(users);
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", users.getId());
        payload.put("name", users.getName());
        String token = TokenUtil.createToken(payload);
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("token", token);
        response.getWriter().println(JSONObject.toJSONString(users));
    }
}
