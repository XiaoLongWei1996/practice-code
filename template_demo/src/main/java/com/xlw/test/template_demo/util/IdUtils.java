package com.xlw.test.template_demo.util;


import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @description: Id获取工具类
 * @Title: IdUtils
 * @Author xlw
 * @Package com.xlw.test.template_demo.util
 * @Date 2025/2/14 17:22
 */
public class IdUtils {

    private static StringRedisTemplate redisTemplate;

    public static StringRedisTemplate redisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextUtil.getBean(StringRedisTemplate.class);
        }
        return redisTemplate;
    }

    public static String safeId(String prefix) {
        //当前时间戳


        //通过当前时间戳获取redis自增
        StringBuilder id = new StringBuilder();
        id.append(prefix);
        return id.toString();
    }
}
