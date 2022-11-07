package com.springcloud.test.alibabagateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AlibabaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlibabaGatewayApplication.class, args);
    }

}
