package com.xlw.test.redis_cache_test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//暴露代理对象
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
@MapperScan("com.xlw.test.redis_cache_test.dao")
@SpringBootApplication
public class RedisCacheTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheTestApplication.class, args);
    }

}
