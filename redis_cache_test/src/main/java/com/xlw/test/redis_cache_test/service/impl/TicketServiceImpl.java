package com.xlw.test.redis_cache_test.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.redis_cache_test.dao.TicketMapper;
import com.xlw.test.redis_cache_test.entity.Ticket;
import com.xlw.test.redis_cache_test.entity.UserTicket;
import com.xlw.test.redis_cache_test.service.TicketService;
import org.springframework.stereotype.Service;

/**
 * 优惠卷表(Ticket)表服务实现类
 *
 * @author xlw
 * @since 2024-01-22 21:12:01
 */
@Service("ticketService")
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Override
    public IPage<Ticket> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }

    @Override
    public boolean flashSale(UserTicket ut) {
        //1.查询优惠卷库存

        //2.扣减库存

        //3.添加用户与优惠卷的关联关系



        return false;
    }
}

