package com.springcloud.test.alibabaconsumer.controller;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author 肖龙威
 * @date 2022/10/31 15:12
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("query")
    public ResponseEntity<String> test() {
        String result = restTemplate.getForObject("http://xxl_config/config/test", String.class);
        return ResponseEntity.ok(result);
    }
}
