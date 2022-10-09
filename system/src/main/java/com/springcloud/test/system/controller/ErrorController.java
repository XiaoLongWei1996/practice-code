package com.springcloud.test.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author 肖龙威
 * @date 2022/10/09 16:48
 */
@Api(tags = "错误请求")
@RestController
@RequestMapping("error")
public class ErrorController {


    @ApiOperation(value = "测试异步请求", notes = "测试异步请求")
    @GetMapping("info")
    public Mono<String> info() throws InterruptedException {
        return Mono.just("请求失败");
    }
}
