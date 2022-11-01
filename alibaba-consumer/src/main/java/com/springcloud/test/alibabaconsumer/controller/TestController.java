package com.springcloud.test.alibabaconsumer.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/10/31 15:12
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostConstruct
    public void init() {
        //定义规则
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule();
        rule.setResource("hello");
        rule.setMinRequestAmount(5);
        rule.setSlowRatioThreshold(0.5);

    }

    @SentinelResource("query")
    @GetMapping("query")
    public ResponseEntity<String> test() {
        String result = restTemplate.getForObject("http://config/config/test", String.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("query1")
    public ResponseEntity<String> test1() {
        Entry entry = null;
        try {
            entry = SphU.entry("hello");
            System.out.println("hello");
        } catch (BlockException e) {
            e.printStackTrace();
        } finally {
            entry.exit();
            ContextUtil.exit();
        }
        return ResponseEntity.ok("result");
    }
}
