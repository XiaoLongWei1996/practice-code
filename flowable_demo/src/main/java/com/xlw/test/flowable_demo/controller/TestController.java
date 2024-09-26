package com.xlw.test.flowable_demo.controller;

import liquibase.pro.packaged.S;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @description:
 * @Title: TestController
 * @Author xlw
 * @Package com.xlw.test.flowable_demo.controller
 * @Date 2024/9/26 14:12
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("t1")
    public BigDecimal t1(BigDecimal num) {
        return num;
    }
}
