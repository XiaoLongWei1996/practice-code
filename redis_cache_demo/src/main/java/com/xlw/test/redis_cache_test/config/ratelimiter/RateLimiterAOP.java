package com.xlw.test.redis_cache_test.config.ratelimiter;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 限流aop
 * @Title: RateLimiterAOP
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.config.ratelimiter
 * @Date 2024/1/31 14:45
 */
@Component
@Aspect
@Order(1)
@Slf4j
public class RateLimiterAOP {

    @Pointcut("@annotation(rateLimiter)")
    private void rateLimiterPointCut(RateLimiter rateLimiter){}

    //限流map
    private final ConcurrentHashMap<String, com.google.common.util.concurrent.RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap<>();

    @Around("rateLimiterPointCut(rateLimiter)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, RateLimiter rateLimiter) {
        com.google.common.util.concurrent.RateLimiter rl = RATE_LIMITER_MAP.computeIfAbsent(rateLimiter.name(), k -> com.google.common.util.concurrent.RateLimiter.create(rateLimiter.limit()));
        boolean b = rl.tryAcquire();
        try {
            if (!b) {
                this.responseFail("频繁访问，稍后再试");
                return null;
            }
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void responseFail(String message) throws IOException {
        HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        JSONObject object = new JSONObject();
        object.put("body",message);
        object.put("code",400);
        object.put("message", "失败");
        pw.write(object.toJSONString());
        pw.flush();
        pw.close();
    }


}
