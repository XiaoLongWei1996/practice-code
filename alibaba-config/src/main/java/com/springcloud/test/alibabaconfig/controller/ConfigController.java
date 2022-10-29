package com.springcloud.test.alibabaconfig.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("test")
    public ResponseEntity<String> test() {
        System.out.println(a);
        System.out.println(b);
        return ResponseEntity.ok(b);
    }
}
