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

    /**
     * 根据id查询
     *
     * @param id id
     * @return {@link User}
     */
    User selectOne(Integer id);

    boolean update(User user);

    boolean delete(Integer id);
}

