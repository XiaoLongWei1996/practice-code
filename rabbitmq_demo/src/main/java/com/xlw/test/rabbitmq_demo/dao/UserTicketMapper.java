package com.xlw.test.rabbitmq_demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlw.test.rabbitmq_demo.entity.UserTicket;
import org.apache.ibatis.annotations.Select;

/**
 * 抢票关联表(UserTicket)表数据库访问层
 *
 * @author xlw
 * @since 2024-01-30 18:47:09
 */
public interface UserTicketMapper extends BaseMapper<UserTicket> {
    
    @Select("select user_id, ticket_id from user_ticket")
    IPage<UserTicket> listByPage(IPage<?> page);
    
}

