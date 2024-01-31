package com.xlw.test.redis_cache_test;

import com.xlw.test.redis_cache_test.util.RabbitMQUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedisCacheTestApplicationTests {

    @Resource
    private RabbitMQUtil rabbitMQUtil;

    @Test
    void contextLoads() {

        rabbitMQUtil.ttlSend("你好", 5000);
    }

}
