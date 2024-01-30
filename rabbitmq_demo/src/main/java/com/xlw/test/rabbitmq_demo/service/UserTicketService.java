package com.xlw.test.rabbitmq_demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.rabbitmq_demo.entity.UserTicket;

/**
 * 抢票关联表(UserTicket)表服务接口
 *
 * @author xlw
 * @since 2024-01-30 18:47:12
 */
public interface UserTicketService extends IService<UserTicket> {
    
     /**
     * 分页查询
     *
     * @param page 页面
     * @return {@link IPage}<{@link UserTicket}>
     */
    IPage<UserTicket> listByPage(IPage<?> page);


}

