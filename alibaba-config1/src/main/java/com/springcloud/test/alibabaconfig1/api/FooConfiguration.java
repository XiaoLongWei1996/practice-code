package com.springcloud.test.alibabaconfig1.api;

import feign.Contract;
import org.springframework.context.annotation.Bean;

//@Configuration
public class FooConfiguration {
    @Bean
    public Contract feignContract1() {
        return new feign.Contract.Default();
    }
}