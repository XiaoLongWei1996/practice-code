package com.springcloud.test.alibabaconfig.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.springcloud.test.alibabaconfig.util.HandleExceptionUtil;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author 肖龙威
 * @date 2022/11/03 15:58
 */
@Configuration
public class WebMVCConfig {

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(fallback = "handleException", fallbackClass = HandleExceptionUtil.class)
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setConnectTimeout(3000);
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(factory);
        return template;
    }

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

}
