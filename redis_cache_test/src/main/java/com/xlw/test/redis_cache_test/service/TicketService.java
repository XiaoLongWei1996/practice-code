package com.xlw.test.redis_cache_test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.redis_cache_test.entity.Ticket;

/**
 * 优惠卷表(Ticket)表服务接口
 *
 * @author xlw
 * @since 2024-01-22 21:12:01
 */
public interface TicketService extends IService<Ticket> {
    
     /**
     * 分页查询
     *
     * @param page 页面
     * @return {@link IPage}<{@link Ticket}>
     */
    IPage<Ticket> listByPage(IPage<?> page);
}

