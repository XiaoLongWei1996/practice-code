package com.xlw.test.redis_cache_test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.redis_cache_test.entity.Ticket;
import com.xlw.test.redis_cache_test.entity.UserTicket;

import java.util.List;

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

    /**
     * 秒杀
     *
     * @return boolean
     */
    String flashSale(UserTicket ut);

    /**
     * 添加票
     *
     * @param ticket 票
     * @return {@link Ticket}
     */
    Ticket addTicket(Ticket ticket);

    /**
     * 秒杀优化
     *
     * @param ut ut
     * @return {@link String}
     */
    String flashSale2(UserTicket ut);

    /**
     * 删除
     *
     * @param idList id列表
     * @return boolean
     */
    boolean delete(List<Integer> idList);

    /**
     * 更新
     *
     * @param ticket 票
     * @return {@link Ticket}
     */
    boolean update(Ticket ticket);
}

