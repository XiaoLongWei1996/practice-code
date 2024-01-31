package com.xlw.test.redis_cache_test.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.xlw.test.redis_cache_test.dao.TicketMapper;
import com.xlw.test.redis_cache_test.entity.Ticket;
import com.xlw.test.redis_cache_test.entity.UserTicket;
import com.xlw.test.redis_cache_test.service.TicketService;
import com.xlw.test.redis_cache_test.service.UserTicketService;
import com.xlw.test.redis_cache_test.util.RabbitMQUtil;
import com.xlw.test.redis_cache_test.util.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 优惠卷表(Ticket)表服务实现类
 *
 * @author xlw
 * @since 2024-01-22 21:12:01
 */
@Service("ticketService")
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Resource
    private UserTicketService userTicketService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RabbitMQUtil rabbitMQUtil;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 锁池
     */
    private final Interner<String> pool = Interners.newWeakInterner();

    /**
     * 票前缀
     */
    private final String TICKET_PREFIX = "ticket:";

    /**
     * 用户票前缀
     */
    private final String USER_TICKET_PREFIX = "user_ticket:";

    private final String LUA_PATH = "lua/FlashSale.lua";

    @Override
    public IPage<Ticket> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }


    @Override
    public String flashSale(UserTicket ut) {
        //加锁的意义是控制一人一单，缺点线程压积
        synchronized (pool.intern("user_" + ut.getUserId())) {
            //1.查询优惠卷库存
            Ticket ticket = getById(ut.getTicketId());
            if (ticket.getTicketCount() <= 0) {
                return "库存不足";
            }
            //2.一人一单
            QueryWrapper<UserTicket> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", ut.getUserId());
            boolean exists = userTicketService.exists(queryWrapper);
            if (exists) {
                return "已经抢过票了";
            }

            //注意:this.submitOrder()会导致事务失效，原因是自引用不走代理
            TicketServiceImpl ts = (TicketServiceImpl) AopContext.currentProxy();
            return ts.submitOrder(ut) ? "抢购成功" : "抢购失败";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean submitOrder(UserTicket ut) {
        //3.扣减库存,乐观锁控制库存的扣减
        boolean update = update().setSql("ticket_count = ticket_count - 1").eq("id", ut.getTicketId()).gt("ticket_count", 0).update();
        //4.添加用户与优惠卷的关联关系
        if (!update) {
            return false;
        }
        return userTicketService.save(ut);
    }

    @Override
    public String flashSale2(UserTicket ut) {
        RLock lock = redissonClient.getLock("lock:" + ut.getUserId());
        boolean b = false;
        try {
            b = lock.tryLock();
            if (!b) {
                return "不允许重复操作";
            }
            //1.调用秒杀lua脚本
            Long result = redisUtil.execute(Long.class, LUA_PATH, CollectionUtil.toList(TICKET_PREFIX + ut.getTicketId(), USER_TICKET_PREFIX + ut.getTicketId()), ut.getTicketId(), ut.getUserId());
            if (result.intValue() != 0) {
                //下单失败
                return result.intValue() == 1 ? "库存不足" : "用户已下单";
            }
            //下单成功，通过MQ完成下单和库存扣减
            rabbitMQUtil.safeSend(ut);
            return "下单成功";
        } finally {
            if (b) {
                lock.unlock();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Ticket addTicket(Ticket ticket) {
        //保存票
        boolean save = save(ticket);
        if (!save) {
            throw new RuntimeException("添加失败");
        }
        //添加redis数据
        redisUtil.set(TICKET_PREFIX + ticket.getId(), ticket.getTicketCount());
        return ticket;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(List<Integer> idList) {
        boolean delete = this.removeByIds(idList);
        if (delete) {
            String[] keys = idList.stream().map(id -> TICKET_PREFIX + id).toArray(String[]::new);
            redisUtil.delete(keys);
        }
        return delete;
    }

    @Override
    public boolean update(Ticket ticket) {
        boolean b = updateById(ticket);
        if (b) {
            redisUtil.set(TICKET_PREFIX + ticket.getId(), ticket.getTicketCount());
        }
        return b;
    }
}

