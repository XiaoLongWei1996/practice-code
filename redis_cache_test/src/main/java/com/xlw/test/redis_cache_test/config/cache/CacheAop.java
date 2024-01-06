package com.xlw.test.redis_cache_test.config.cache;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xlw.test.redis_cache_test.util.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @description: 缓存处理AOP
 * @Title: CacheAop
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.config.cache
 * @Date 2024/1/6 17:48
 */
@Component
@Aspect
@Order(1)
public class CacheAop {


    @Resource
    private RedisUtil redisUtil;

    @Pointcut("@annotation(cache)")
    private void cachePointCut(Cache cache){}

    @Pointcut("@annotation(com.xlw.test.redis_cache_test.config.cache.CachePut)")
    private void cachePutPointCut(){}

    @Pointcut("@annotation(com.xlw.test.redis_cache_test.config.cache.CacheClean)")
    private void cacheCleanPointCut(){}

    private static final String CONCAT = "_";

    //缓存前置通知
    @Around("cachePointCut(cache)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Cache cache) {
        String key = cache.key();
        if (StrUtil.isBlank(key)) {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        String prefix = cache.prefix();
        String cacheName = StrUtil.isBlank(prefix) ? key : prefix + CONCAT + key;
        //查询缓存
        Object o = redisUtil.get(cacheName);
        if (ObjectUtil.isNotNull(o)) {
            return o;
        }

        try {
            return Optional.ofNullable(o).orElseGet(() -> {
                try {
                    //查询数据库
                    Object result = proceedingJoinPoint.proceed();
                    //防止缓存穿透
                    redisUtil.set(cacheName, result, cache.timeout(), cache.timeUnit());
                    return result;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
