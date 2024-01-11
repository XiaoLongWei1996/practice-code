package com.xlw.test.redis_cache_test.config.cache;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xlw.test.redis_cache_test.util.RedisUtil;
import com.xlw.test.redis_cache_test.util.SpELUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
@Slf4j
public class CacheAop {


    @Resource
    private RedisUtil redisUtil;

    @Pointcut("@annotation(cache)")
    private void cachePointCut(Cache cache){}

    @Pointcut("@annotation(cachePut)")
    private void cachePutPointCut(CachePut cachePut){}

    @Pointcut("@annotation(cacheClean)")
    private void cacheCleanPointCut(CacheClean cacheClean){}

    private static final String CONCAT = "_";

    //缓存环绕通知
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
        String cacheName = getCacheKey(cache.prefix(), key, proceedingJoinPoint);
//        log.info("缓存名称：{}", cacheName);
        //查询缓存
        Object o = redisUtil.get(cacheName);
        if (ObjectUtil.isNotNull(o)) {
            return o;
        }
        try {
            synchronized (cacheName.intern()) {
                //查询缓存
                o = redisUtil.get(cacheName);
                if (ObjectUtil.isNotNull(o)) {
                    return o;
                }
                //查询数据库
                Object result = cachePenetration(proceedingJoinPoint);
                //缓存结果
                redisUtil.setNx(cacheName, result, cache.timeout(), cache.timeUnit());
                return result;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    //更新缓存环绕通知
    @Around("cachePutPointCut(cachePut)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, CachePut cachePut) {
        String key = cachePut.key();
        if (StrUtil.isBlank(key)) {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        String cacheName = getCacheKey(cachePut.prefix(), key, proceedingJoinPoint);
        log.info("缓存名称：{}", cacheName);

        try {
            synchronized (cacheName.intern()) {
                //查询缓存
                if (!redisUtil.hasKey(cacheName)) {
                    try {
                        return proceedingJoinPoint.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                //设置缓存过期时间
                redisUtil.expire(cacheName, cachePut.timeout(), cachePut.timeUnit());
                //修改数据库
                Object result = proceedingJoinPoint.proceed();
                return result;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            //删除缓存
            redisUtil.delete(cacheName);
        }
    }

    @Around("cacheCleanPointCut(cacheClean)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, CacheClean cacheClean) {
        String key = cacheClean.key();
        String cacheName = null;
        if (cacheClean.isAll()) {
            cacheName = cacheClean.prefix() + CONCAT + "*";
        } else {
            cacheName = getCacheKey(cacheClean.prefix(), key, proceedingJoinPoint);
        }
        log.info("缓存名称：{}", cacheName);

        try {
            synchronized (cacheName.intern()) {
                if (cacheClean.before()) {
                    //前置删除
                    if (cacheClean.isAll()) {
                        //删除全部
                        Set<String> keys = redisUtil.keys(cacheName);
                        redisUtil.delete(keys.toArray(new String[keys.size()]));
                    } else {
                        //删除单个
                        redisUtil.delete(cacheName);
                    }
                }
                return proceedingJoinPoint.proceed();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (!cacheClean.before()) {
                //前置删除
                if (cacheClean.isAll()) {
                    //删除全部
                    Set<String> keys = redisUtil.keys(cacheName);
                    redisUtil.delete(keys.toArray(new String[keys.size()]));
                } else {
                    //删除单个
                    redisUtil.delete(cacheName);
                }
            }
        }
    }


    private String getCacheKey(String prefix, String key, ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = proceedingJoinPoint.getArgs();
        Map<String, Object> map = new HashMap<>(parameterNames.length);
        for (int i = 0; i < parameterNames.length; i++) {
            String pm = parameterNames[i];
            Object value = args[i];
            map.put(pm, value);
        }
        String s = key.contains("#") ? SpELUtil.parseSpEL(key, map) : key;
        return StrUtil.isBlank(prefix)? s : prefix + CONCAT + s;
    }

    /**
     * 缓存渗透
     *
     * @param proceedingJoinPoint 继续连接点
     * @return {@link Object}
     */
    private Object cachePenetration(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object obj = proceedingJoinPoint.proceed();
        if (ObjectUtil.isNotNull(obj)) {
            return obj;
        }
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Class returnType = signature.getReturnType();
        return returnType.newInstance();
    }


}
