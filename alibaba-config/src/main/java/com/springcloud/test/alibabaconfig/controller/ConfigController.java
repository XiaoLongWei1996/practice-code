package com.springcloud.test.alibabaconfig.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 肖龙威
 * @date 2022/10/28 22:20
 */
@RestController
@RequestMapping("config")
public class ConfigController {

    @Value("${a}")
    private String a;

    @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(a);
    }
}
