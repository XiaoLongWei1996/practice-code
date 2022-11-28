package com.springcloud.test.alibabaconsumer.config.security;

import cn.hutool.core.util.StrUtil;
import com.springcloud.test.alibabaconsumer.entity.Users;
import com.springcloud.test.alibabaconsumer.service.UsersService;
import com.springcloud.test.alibabaconsumer.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
        String token = request.getHeader("token");
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        try {
            if (StrUtil.isBlank(token) || !TokenUtil.validateToken(token)) {
                writer.print("安全退出");
                return;
            }
            Users u = TokenUtil.parseToken(token, Users.class);
            u.setState(0);
            u.setLogoutDt(new Date());
            u.setUpdateDt(new Date());
            u.setToken("");
            usersService.updateById(u);
            writer.print("安全退出");
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
