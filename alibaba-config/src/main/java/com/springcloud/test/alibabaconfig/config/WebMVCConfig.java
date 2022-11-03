package com.springcloud.test.alibabaconfig.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
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
    @SentinelRestTemplate(fallback = "handleException", fallbackClass = WebMVCConfig.class)
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setConnectTimeout(3000);
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(factory);
        return template;
    }

    public static ClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException exception) {
        exception.printStackTrace();
        return new SentinelClientHttpResponse("服务器繁忙");
    }
}
