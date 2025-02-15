package com.xlw.test.template_demo.util;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description: Id获取工具类
 * @Title: IdUtils
 * @Author xlw
 * @Package com.xlw.test.template_demo.util
 * @Date 2025/2/14 17:22
 */
public class IdUtils {

    private static StringRedisTemplate redisTemplate;

    /**
     * id 前缀
     */
    private static final String ID_PREFIX = "ID_INCREASE:";

    public static StringRedisTemplate redisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextUtil.getBean(StringRedisTemplate.class);
        }
        return redisTemplate;
    }

    public static String safeId(String prefix) {
        StringBuilder id = new StringBuilder();
        id.append(prefix);
        //当前时间戳
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_MS_PATTERN));
        id.append(timestamp);
        String serialNumber = redisSerialNumber(timestamp);
        id.append(serialNumber);
        return id.toString();
    }

    /**
     * Redis 序列号
     *
     * @param key key
     * @return {@link String }
     */
    private static String redisSerialNumber(String key) {
        //通过当前时间戳获取redis自增
        DefaultRedisScript script = new DefaultRedisScript();
        script.setResultType(String.class);
        script.setLocation(ApplicationContextUtil.getApplicationContext().getResource("classpath:script/id_increase.lua"));
        String serialNumber = (String) redisTemplate().execute(script, ListUtil.toList(ID_PREFIX + key));
        return serialNumber;
    }

}
