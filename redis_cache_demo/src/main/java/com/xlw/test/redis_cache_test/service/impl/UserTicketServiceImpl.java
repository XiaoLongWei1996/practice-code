package com.xlw.test.redis_cache_test.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.redis_cache_test.dao.UserTicketMapper;
import com.xlw.test.redis_cache_test.entity.UserTicket;
import com.xlw.test.redis_cache_test.service.UserTicketService;
import org.springframework.stereotype.Service;

/**
 * 抢票关联表(UserTicket)表服务实现类
 *
 * @author xlw
 * @since 2024-01-22 21:12:26
 */
@Service("userTicketService")
public class UserTicketServiceImpl extends ServiceImpl<UserTicketMapper, UserTicket> implements UserTicketService {

    @Override
    public IPage<UserTicket> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }
}

