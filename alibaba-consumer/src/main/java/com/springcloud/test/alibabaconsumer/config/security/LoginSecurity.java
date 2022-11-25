package com.springcloud.test.alibabaconsumer.config.security;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.springcloud.test.alibabaconsumer.entity.Users;
import com.springcloud.test.alibabaconsumer.service.UsersService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 对登录的账号密码进行验证
 * @author 肖龙威
 * @date 2022/11/24 14:56
 */
@Component
public class LoginSecurity implements UserDetailsService {

    @Resource
    private UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Users::getUserName, username);
        Users users = usersService.getOne(wrapper);
        if (ObjectUtil.isEmpty(users)) {
            throw new RuntimeException("用户名或者密码错误");
        }
        return new LoginUser(users.getUserName(), users.getPassword(), AuthorityUtils.createAuthorityList(), users);
    }
}
