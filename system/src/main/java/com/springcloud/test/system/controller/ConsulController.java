package com.springcloud.test.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.springcloud.test.system.config.feign.HomeApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private HomeApi homeApi;

//    @ApiOperation(value = "获取token", notes = "获取token")
//    @GetMapping("getToken")
//    public ResponseEntity<String> getToken() {
//        String token = restTemplate.getForObject("http://home/tool/acquireToken", String.class);
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("token", token);
//        return ResponseEntity.ok(JSONObject.toJSONString(map));
//    }

    @ApiOperation(value = "feign获取token", notes = "feign获取token")
    @GetMapping("getToken01")
    public ResponseEntity<String> getToken01() {
        String token = homeApi.getToken();
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return ResponseEntity.ok(JSONObject.toJSONString(map));
    }
}
