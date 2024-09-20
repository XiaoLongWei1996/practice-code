package com.xlw.test.redis_cache_test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlw.test.redis_cache_test.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 * 用户表(User)表数据库访问层
 *
 * @author xlw
 * @since 2023-12-23 19:43:50
 */
public interface UserMapper extends BaseMapper<User> {
    
    @Select("select name, age, phone, user_name, password, create_dt, update_dt from user")
    IPage<User> listByPage(IPage<?> page);
    
}

