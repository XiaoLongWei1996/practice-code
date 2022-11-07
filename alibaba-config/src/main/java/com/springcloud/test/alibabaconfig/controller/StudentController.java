package com.springcloud.test.alibabaconfig.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 肖龙威
 * @date 2022/11/07 13:16
 */
@RestController
@RequestMapping("student")
public class StudentController {

    @GetMapping("query")
    public ResponseEntity<String> query(Integer id) {
        String result = String.format("{id:%d}", id);
        return ResponseEntity.ok(result);
    }
}
