package com.xlw.test.redis_cache_test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlw.test.redis_cache_test.entity.Ticket;
import org.apache.ibatis.annotations.Select;

/**
 * 优惠卷表(Ticket)表数据库访问层
 *
 * @author xlw
 * @since 2024-01-22 21:12:01
 */
public interface TicketMapper extends BaseMapper<Ticket> {
    
    @Select("select name, ticket_count, create_dt, update_dt from ticket")
    IPage<Ticket> listByPage(IPage<?> page);
    
}

