package com.springcloud.test.alibabaconfig.config;

import feign.Contract;

/**
 * @author 肖龙威
 * @date 2022/11/04 14:57
 */
//@Configuration
public class FeignConfig {

    //@Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }
}
