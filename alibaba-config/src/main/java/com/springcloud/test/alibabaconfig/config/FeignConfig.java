package com.springcloud.test.alibabaconfig.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 肖龙威
 * @date 2022/11/04 14:57
 */
@Configuration
public class FeignConfig {

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }
}
