package com.springcloud.test.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 肖龙威
 * @date 2022/09/02 14:51
 */
@RequestMapping("parameter")
@RestController
public class ParameterController {

    @GetMapping("queryParam")
    public ResponseEntity<Integer> queryParam() {
        return ResponseEntity.ok(1);
    }
}
