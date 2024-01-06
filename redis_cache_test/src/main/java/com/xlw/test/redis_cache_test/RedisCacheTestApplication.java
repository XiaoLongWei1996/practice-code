package com.xlw.test.redis_cache_test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.xlw.test.redis_cache_test.dao")
@SpringBootApplication
public class RedisCacheTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheTestApplication.class, args);
    }

}
