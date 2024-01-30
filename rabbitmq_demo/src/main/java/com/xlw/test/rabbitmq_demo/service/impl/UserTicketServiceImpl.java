package com.xlw.test.rabbitmq_demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.rabbitmq_demo.dao.UserTicketMapper;
import com.xlw.test.rabbitmq_demo.entity.UserTicket;
import com.xlw.test.rabbitmq_demo.service.UserTicketService;
import org.springframework.stereotype.Service;

/**
 * 抢票关联表(UserTicket)表服务实现类
 *
 * @author xlw
 * @since 2024-01-30 18:47:14
 */
@Service("userTicketService")
public class UserTicketServiceImpl extends ServiceImpl<UserTicketMapper, UserTicket> implements UserTicketService {

    @Override
    public IPage<UserTicket> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }
}

