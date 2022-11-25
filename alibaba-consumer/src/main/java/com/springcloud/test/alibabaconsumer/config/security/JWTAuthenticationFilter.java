package com.springcloud.test.alibabaconsumer.config.security;

import cn.hutool.core.util.StrUtil;
import com.springcloud.test.alibabaconsumer.entity.Users;
import com.springcloud.test.alibabaconsumer.service.UsersService;
import com.springcloud.test.alibabaconsumer.util.TokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 肖龙威
 * @date 2022/11/25 21:30
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token) || !TokenUtil.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }
        Users users = TokenUtil.parseToken(token, Users.class);
        Users u = usersService.getById(users.getId());
        LoginUser principal = new LoginUser(u.getUserName(), u.getPassword(), AuthorityUtils.createAuthorityList(), u);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
