package com.springcloud.test.alibabaconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AlibabaConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlibabaConfigApplication.class, args);
    }

}
