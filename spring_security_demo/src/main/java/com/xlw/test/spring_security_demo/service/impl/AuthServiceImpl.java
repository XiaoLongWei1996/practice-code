package com.xlw.test.spring_security_demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.xlw.test.spring_security_demo.config.Cache;
import com.xlw.test.spring_security_demo.entity.User;
import com.xlw.test.spring_security_demo.entity.UserInfo;
import com.xlw.test.spring_security_demo.service.AuthService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @Title: AuthServiceImpl
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.service.impl
 * @Date 2024/9/20 21:58
 */
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    /**
     * 按用户名加载用户
     *
     * @param username 用户名
     * @return {@link UserDetails }
     * @throws UsernameNotFoundException 未找到用户名异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //模拟从数据库取数据
        User user = Cache.DATABASE.get(username);
        if (user == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(user, userInfo, false);
        //权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        //添加角色
        List<String> roles = Cache.USER_ROLE_DATABASE.get(userInfo.getUsername());
        if (CollectionUtil.isNotEmpty(roles)) {
            for (String role : roles) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                authorities.add(authority);
            }
        }
        //添加权限
        List<String> permissions = Cache.USER_PERMISSIONS_DATABASE.get(userInfo.getUsername());
        if (CollectionUtil.isNotEmpty(permissions)) {
            for (String permission : permissions) {
                GrantedAuthority authority = new SimpleGrantedAuthority(permission);
                authorities.add(authority);
            }
        }
        //设置用户拥有的权限
        userInfo.setAuthorities(authorities);
        return userInfo;
    }
}
