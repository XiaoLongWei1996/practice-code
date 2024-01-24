package com.xlw.test.log4j2_test.aop;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @description: 切面记录日志
 * @Title: ResultAdvice
 * @Author xlw
 * @Package com.xlw.test.search.config.aop
 * @Date 2023/8/23 16:57
 */
@Slf4j
@Component
@Aspect
@Order(1)
public class LogAdvice {


    /**
     * 连接点
     */
    @Pointcut("execution(public * com.xlw.test.log4j2_test.controller.*.*(..))")
    public void controller() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     */
    @Around("controller()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        JSONObject obj = JSONUtil.createObj();
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class declaringType = methodSignature.getDeclaringType();
        //记录class名称
        obj.set("class", declaringType.getName());
        String methodName = methodSignature.getMethod().getName();
        //记录方法名称
        obj.set("method", methodName);
        //获取参数
        String[] parameterNames = methodSignature.getParameterNames();
        JSONObject p = JSONUtil.createObj();
        if (parameterNames != null && parameterNames.length > 0) {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                String name = parameterNames[i];
                Object arg = args[i];
                p.set(name, arg);
            }
        }
        obj.set("params", p);
        Object result = null;
        try {
            //获得返回值
            result = joinPoint.proceed();
            obj.set("return", result);
        } catch (Throwable e) {
            log.error(obj.toString(), e);
            throw e;
        }
        log.info(obj.toString());
        return result;
    }
}
