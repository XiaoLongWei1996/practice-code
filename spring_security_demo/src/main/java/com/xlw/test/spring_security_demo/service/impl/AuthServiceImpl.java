package com.xlw.test.spring_security_demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xlw.test.spring_security_demo.config.Cache;
import com.xlw.test.spring_security_demo.entity.User;
import com.xlw.test.spring_security_demo.entity.UserInfo;
import com.xlw.test.spring_security_demo.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        return userInfo;
    }
}
