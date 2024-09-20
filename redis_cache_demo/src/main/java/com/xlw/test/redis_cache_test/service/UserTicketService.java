package com.xlw.test.redis_cache_test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.redis_cache_test.entity.UserTicket;

/**
 * 抢票关联表(UserTicket)表服务接口
 *
 * @author xlw
 * @since 2024-01-22 21:12:26
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

