package com.springcloud.test.alibabaconsumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author 肖龙威
 * @date 2022/10/31 15:14
 */
@Configuration
public class WebMVCConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(4000);
        clientHttpRequestFactory.setConnectTimeout(2000);
        clientHttpRequestFactory.setBufferRequestBody(true);
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(clientHttpRequestFactory);
        return template;
    }
}
