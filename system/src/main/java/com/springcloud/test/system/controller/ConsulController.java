package com.springcloud.test.system.controller;

import com.springcloud.test.system.config.feign.HomeApi;
import com.springcloud.test.system.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    @Autowired
    private Test test;

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
    public ResponseEntity<Result<Map<String, String>>> getToken01() {
        String token = homeApi.getToken();
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        Result<Map<String, String>> ok = Result.ok(map);
        return ResponseEntity.ok(ok);
    }

    @ApiOperation(value = "测试限速器", notes = "测试限速器")
    @GetMapping("getToken02")
    public ResponseEntity<String> getToken02() throws InterruptedException, ExecutionException {
        String token = homeApi.getToken01().get();
        return ResponseEntity.ok(token);
    }

    @ApiOperation(value = "测试同步请求", notes = "测试同步请求")
    @GetMapping("testSyn")
    public String testSyn() throws InterruptedException, ExecutionException {
        CompletableFuture<String> token = test.getToken();
        return token.get();
    }

    @ApiOperation(value = "测试异步请求", notes = "测试异步请求")
    @GetMapping("/testAsyn")
    public Mono<String> testAsyn() throws InterruptedException {
        Thread.sleep(2000);
        return Mono.just("ok");
    }

}
