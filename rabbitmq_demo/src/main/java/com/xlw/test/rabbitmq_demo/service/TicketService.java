package com.xlw.test.rabbitmq_demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.rabbitmq_demo.entity.Ticket;
import com.xlw.test.rabbitmq_demo.entity.UserTicket;

/**
 * 优惠卷表(Ticket)表服务接口
 *
 * @author xlw
 * @since 2024-01-30 19:07:59
 */
public interface TicketService extends IService<Ticket> {
    
     /**
     * 分页查询
     *
     * @param page 页面
     * @return {@link IPage}<{@link Ticket}>
     */
    IPage<Ticket> listByPage(IPage<?> page);

    /**
     * 创建订单
     *
     * @param userTicket 用户票
     */
    boolean createOrder(UserTicket userTicket);
}

