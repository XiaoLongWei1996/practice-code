package com.xlw.test.template_demo.util;


import com.xlw.test.template_demo.cons.TaskNotReturn;
import com.xlw.test.template_demo.cons.TaskReturn;
import com.xlw.test.template_demo.exception.BusinessException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: redis锁工具类
 * @Title: RedisLockUtil
 * @Author xlw
 * @Package com.sxkj.pay.util
 * @Date 2024/8/8 14:32
 */
public class RedisLockUtil {

    private static RedissonClient redissonClient;

    private static RedissonClient getRedissonClient() {
        if (Objects.isNull(redissonClient)) {
            redissonClient = ApplicationContextUtil.getBean(RedissonClient.class);
        }
        return redissonClient;
    }

    public static void withLockExecute(String lockKey, long timeout, TaskNotReturn task) {
        RLock lock = getRedissonClient().getLock(lockKey);
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

    public static <R> R withLockExecute(String lockKey, long timeout, TaskReturn<R> task) {
        RLock lock = getRedissonClient().getLock(lockKey);
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
