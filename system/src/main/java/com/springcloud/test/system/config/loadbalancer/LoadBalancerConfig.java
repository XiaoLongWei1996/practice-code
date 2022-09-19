package com.springcloud.test.system.config.loadbalancer;

import org.springframework.context.annotation.Configuration;

/**
 * @author 肖龙威
 * @date 2022/09/16 15:59
 */
@Configuration
//@LoadBalancerClient(value = "home"/*, configuration = RandomConfig.class*/)
public class LoadBalancerConfig {

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        //读超时
//        factory.setReadTimeout(5000);
//        //连接超时
//        factory.setConnectTimeout(15000);
//        RestTemplate restTemplate = new RestTemplate(factory);
//        return restTemplate;
//    }

}
