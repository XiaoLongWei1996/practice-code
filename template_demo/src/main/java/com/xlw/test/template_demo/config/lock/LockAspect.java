package com.xlw.test.template_demo.config.lock;


import com.xlw.test.template_demo.exception.BusinessException;
import com.xlw.test.template_demo.util.RedisLockUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description: 分布式锁切面
 * @Title: LockAspect
 * @Author xlw
 * @Package com.xlw.test.template_demo.config.lock
 * @Date 2024/12/19 15:05
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(lock)")
    public void lockPoint(Lock lock) {}

    @Around(value = "lockPoint(lock)", argNames = "point,lock")
    public Object around(ProceedingJoinPoint point, Lock lock) throws Throwable {
        RLock rLock = redissonClient.getLock(lock.name());
        Object result = null;
        try {
            boolean b = rLock.tryLock(lock.timeout(), lock.unit());
            if (!b) {
                throw new BusinessException("获取锁失败");
            }
            result = point.proceed();
        } finally {
            if (Objects.nonNull(rLock)) {
                rLock.unlock();
            }
        }
        return result;
    }
}
