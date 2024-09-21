package com.xlw.test.spring_security_demo.config;

import cn.hutool.core.collection.ListUtil;
import com.xlw.test.spring_security_demo.entity.User;
import com.xlw.test.spring_security_demo.entity.UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 缓存
 * @Title: Cache
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.config
 * @Date 2024/9/20 18:06
 */
public class Cache {

    public static final ConcurrentHashMap<String, UserInfo> TOKEN_CACHE = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, User> DATABASE = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, List<String>> USER_ROLE_DATABASE = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, List<String>> USER_PERMISSIONS_DATABASE = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, String> SMS_CACHE = new ConcurrentHashMap<>();

    /**
     * 路径白名单
     */
    public static final String[] PATH_WHITELIST = {
            "/sys/login"
    };

    static {
        DATABASE.put("xlw", new User("xlw", "e10adc3949ba59abbe56e057f20f883e", "小明", 18, true, true, true, true));

        USER_ROLE_DATABASE.put("xlw", ListUtil.of("ROLE_ADMIN"));

        USER_PERMISSIONS_DATABASE.put("xlw", ListUtil.of("sys:test"));
    }
}
