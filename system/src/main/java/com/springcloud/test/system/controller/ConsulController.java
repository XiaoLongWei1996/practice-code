package com.springcloud.test.system.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 肖龙威
 * @date 2022/09/14 14:47
 */
@Api(tags = "consule测试")
@RestController
@RequestMapping("consul")
public class ConsulController {

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "获取token", notes = "获取token")
    @GetMapping("getToken")
    public ResponseEntity<String> getToken() {
        String token = restTemplate.getForObject("http://home/tool/acquireToken", String.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return ResponseEntity.ok(JSONObject.toJSONString(map));
    }
}
