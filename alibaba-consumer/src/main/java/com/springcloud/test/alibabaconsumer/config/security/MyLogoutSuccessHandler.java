package com.springcloud.test.alibabaconsumer.config.security;

import com.springcloud.test.alibabaconsumer.entity.Users;
import com.springcloud.test.alibabaconsumer.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/11/25 11:22
 */
@RequiredArgsConstructor
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UsersService usersService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Users users = loginUser.getUsers();
        users.setState(0);
        users.setLogoutDt(new Date());
        users.setUpdateDt(new Date());
        usersService.updateById(users);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("安全退出");
    }
}
