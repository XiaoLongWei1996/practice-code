package com.springcloud.test.home;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest
class HomeApplicationTests {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    void contextLoads() {
        List<String> services = discoveryClient.getServices();
        System.out.println(services);
        List<ServiceInstance> home = discoveryClient.getInstances("home");
        for (ServiceInstance serviceInstance : home) {
            System.out.println(serviceInstance.getUri());
        }
    }

}
