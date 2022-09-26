package com.springcloud.test.home.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 肖龙威
 * @date 2022/09/14 14:39
 */

@RestController
@RequestMapping("tool")
public class ToolController {

    @Value("${server.port}")
    private Integer port;

    private AtomicInteger count = new AtomicInteger(0);

    @GetMapping("acquireToken")
    public ResponseEntity<String> acquireToken() throws InterruptedException {
        Thread.sleep(2000);
        int i = RandomUtil.randomInt(10);
        System.out.println("执行" + count.incrementAndGet());
        String token = RandomUtil.randomString(8);
        return ResponseEntity.ok(token + port);
    }

}
