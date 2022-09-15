package com.springcloud.test.center.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 肖龙威
 * @date 2022/09/14 14:39
 */

@RestController
@RequestMapping("tool")
public class ToolController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("acquireToken")
    public ResponseEntity<String> acquireToken() {
        String token = RandomUtil.randomString(8);
        return ResponseEntity.ok(token + port);
    }

}
