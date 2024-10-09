package com.xlw.test.template_demo.util;


import com.xlw.test.template_demo.cons.TaskNotReturn;
import com.xlw.test.template_demo.cons.TaskReturn;
import com.xlw.test.template_demo.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @description: redis锁工具类
 * @Title: RedisLockUtil
 * @Author xlw
 * @Package com.sxkj.pay.util
 * @Date 2024/8/8 14:32
 */
@AllArgsConstructor
public class RedisLockUtil {

    private final RedissonClient redissonClient;

    public void withLockExecute(String lockKey, long timeout, TaskNotReturn task) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean b = false;
        try {
            b = lock.tryLock(timeout, TimeUnit.SECONDS);
            if (!b) {
                throw new BusinessException("获取锁失败");
            }
            task.execute();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (b) {
                lock.unlock();
            }
        }
    }

    public <R> R withLockExecute(String lockKey, long timeout, TaskReturn<R> task) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean b = false;
        try {
            b = lock.tryLock(timeout, TimeUnit.SECONDS);
            if (!b) {
                throw new BusinessException("获取锁失败");
            }
            return task.execute();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (b) {
                lock.unlock();
            }
        }
    }

}
