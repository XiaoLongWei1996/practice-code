package com.xlw.test.redis_cache_test.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.redis_cache_test.dao.UserMapper;
import com.xlw.test.redis_cache_test.entity.User;
import com.xlw.test.redis_cache_test.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author xlw
 * @since 2023-12-23 19:43:54
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public IPage<User> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }
}

