package com.xlw.test.template_demo.util;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import org.jetbrains.annotations.NotNull;
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
        String serialNumber = redisSerialNumber(timestamp, 5);
        id.append(serialNumber);
        return id.toString();
    }

    /**
     * Redis 序列号
     *
     * @param key key
     * @param len 序列长度
     * @return {@link String }
     */
    @NotNull
    private static String redisSerialNumber(String key, int len) {
        //脚本
        String jb = "--key\n" +
                "local idKey = KEYS[1]\n" +
                "local len = ARGV[1]\n" +
                "local formatStr = '%0' .. len .. 'd'\n" +
                "-- 过期时间\n" +
                "local timeout = 3;\n" +
                "if (redis.call('EXISTS', idKey) == 1) then\n" +
                "    -- 存在，加1\n" +
                "    redis.call('INCR', idKey)\n" +
                "    redis.call('expire', idKey, timeout)\n" +
                "else\n" +
                "    -- 不存在\n" +
                "    redis.call('set', idKey, 1)\n" +
                "    redis.call('expire', idKey, timeout)\n" +
                "end\n" +
                "local value = redis.call('get', idKey)\n" +
                "-- 补零\n" +
                "return string.format(formatStr, value)";
        //通过当前时间戳获取redis自增
        DefaultRedisScript script = new DefaultRedisScript();
        script.setResultType(String.class);
        //script.setLocation(ApplicationContextUtil.getApplicationContext().getResource("classpath:script/id_increase.lua"));
        script.setScriptText(jb);
        String serialNumber = (String) redisTemplate().execute(script, ListUtil.toList(ID_PREFIX + key), String.valueOf(len));
        return serialNumber;
    }

}
