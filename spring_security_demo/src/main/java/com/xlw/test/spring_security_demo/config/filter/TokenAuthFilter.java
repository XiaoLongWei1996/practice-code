package com.xlw.test.spring_security_demo.config.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.xlw.test.spring_security_demo.config.AuthException;
import com.xlw.test.spring_security_demo.config.Cache;
import com.xlw.test.spring_security_demo.config.MultiAuthenticationToken;
import com.xlw.test.spring_security_demo.entity.UserInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * @description: token认证过滤器
 * @Title: TokenAuthFilter
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.config.filter
 * @Date 2024/9/21 0:53
 */
public class TokenAuthFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader("AuthToken");
        if (StrUtil.isNotBlank(authToken)) {
            UserInfo userInfo = Cache.TOKEN_CACHE.get(authToken);
            if (userInfo == null) {
                throw new AuthException("token过期");
            }
            MultiAuthenticationToken authenticationToken = new MultiAuthenticationToken(userInfo.getUsername(), Collections.emptySet());
            //认证
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //放行，交给后面的过滤器处理
        filterChain.doFilter(request, response);
    }
}
