package com.xlw.test.redis_cache_test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlw.test.redis_cache_test.entity.UserTicket;
import org.apache.ibatis.annotations.Select;

/**
 * 抢票关联表(UserTicket)表数据库访问层
 *
 * @author xlw
 * @since 2024-01-22 21:12:26
 */
public interface UserTicketMapper extends BaseMapper<UserTicket> {
    
    @Select("select user_id, ticket_id from user_ticket")
    IPage<UserTicket> listByPage(IPage<?> page);
    
}

