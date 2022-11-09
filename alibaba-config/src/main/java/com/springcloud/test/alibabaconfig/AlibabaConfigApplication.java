package com.springcloud.test.alibabaconfig;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(basePackages = "com.springcloud.test.alibabaconfig.dao")
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AlibabaConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlibabaConfigApplication.class, args);
    }

}
