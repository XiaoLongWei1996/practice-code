package com.xlw.test.template_demo.util;


import com.xlw.test.template_demo.cons.TaskNotReturn;
import com.xlw.test.template_demo.cons.TaskReturn;
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
        RLock lock = redissonClient.getLock(lockKey);
        boolean b = false;
        try {
            // 区分是否使用看门狗：expireSeconds=-1时，不指定过期时间，由看门狗自动续期（默认30秒续期）
            if (timeout == -1) {
                // 立即尝试获取锁，无等待时间，使用看门狗自动续期
                b = lock.tryLock();
            } else {
                // 立即尝试获取锁，指定过期时间（看门狗不生效）
                b = lock.tryLock(0, timeout, TimeUnit.SECONDS);
            }
            if (!b) {
                return;
            }
            task.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lock != null && b && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public static <R> R withLockExecute(String lockKey, long timeout, TaskReturn<R> task) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean b = false;
        try {
            // 区分是否使用看门狗：expireSeconds=-1时，不指定过期时间，由看门狗自动续期（默认30秒续期）
            if (timeout == -1) {
                // 立即尝试获取锁，无等待时间，使用看门狗自动续期
                b = lock.tryLock();
            } else {
                // 立即尝试获取锁，指定过期时间（看门狗不生效）
                b = lock.tryLock(0, timeout, TimeUnit.SECONDS);
            }
            if (!b) {
                return null;
            }
            return task.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lock != null && b && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
