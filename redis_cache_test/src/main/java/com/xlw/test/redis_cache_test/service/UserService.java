package com.xlw.test.redis_cache_test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.redis_cache_test.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author xlw
 * @since 2023-12-23 19:43:50
 */
public interface UserService extends IService<User> {
    
     /**
     * 分页查询
     *
     * @param page 页面
     * @return {@link IPage}<{@link User}>
     */
    IPage<User> listByPage(IPage<?> page);
}

