package com.xlw.test.rabbitmq_demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.rabbitmq_demo.dao.TicketMapper;
import com.xlw.test.rabbitmq_demo.entity.Ticket;
import com.xlw.test.rabbitmq_demo.entity.UserTicket;
import com.xlw.test.rabbitmq_demo.service.TicketService;
import com.xlw.test.rabbitmq_demo.service.UserTicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 优惠卷表(Ticket)表服务实现类
 *
 * @author xlw
 * @since 2024-01-30 19:07:59
 */
@Service("ticketService")
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Resource
    private UserTicketService userTicketService;

    @Override
    public IPage<Ticket> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createOrder(UserTicket userTicket) {
        boolean update = update().setSql("ticket_count = ticket_count - 1")
                .eq("id", userTicket.getTicketId())
                .gt("ticket_count", 0).update();
        if (update) {
            userTicketService.save(userTicket);
            return true;
        }
        return false;
    }
}

