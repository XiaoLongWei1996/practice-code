package com.springcloud.test.alibabaconfig1.controller;

import com.springcloud.test.alibabaconfig1.api.ConsumerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 肖龙威
 * @date 2022/10/28 22:20
 */
@RefreshScope
@RestController
@RequestMapping("config")
public class ConfigController {

    @Value("${a}")
    private String a;

    @Value("${b}")
    private String b;

    @Value("${server.port}")
    private String port;

    @Resource
    private ConsumerApi consumerApi;

    @GetMapping("test")
    public ResponseEntity<String> test() {
        String result = consumerApi.read("app1");
        return ResponseEntity.ok(result);
    }
}
