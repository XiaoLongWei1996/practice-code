package com.springcloud.test.alibabaconfig1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(basePackages = "com.springcloud.test.alibabaconfig1.dao")
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AlibabaConfig1Application {

    public static void main(String[] args) {
        SpringApplication.run(AlibabaConfig1Application.class, args);
    }

}
