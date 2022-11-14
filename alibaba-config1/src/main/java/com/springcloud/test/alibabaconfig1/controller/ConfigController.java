package com.springcloud.test.alibabaconfig1.controller;

import com.springcloud.test.alibabaconfig1.api.ConsumerApi;
import com.springcloud.test.alibabaconfig1.dao.DeptMapper;
import com.springcloud.test.alibabaconfig1.entity.Dept;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 肖龙威
 * @date 2022/10/28 22:20
 */
@RefreshScope
@RestController
@RequestMapping("config")
public class ConfigController {

    @Value("${a}")
    private String a;

    @Value("${b}")
    private String b;

    @Value("${server.port}")
    private String port;

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private ConsumerApi consumerApi;

    @GetMapping("test")
    public ResponseEntity<String> test() {
        Dept dept = new Dept();
        dept.setCode("0002");
        dept.setName("财务");
        deptMapper.insert(dept);
        int i = 1/0;
        return ResponseEntity.ok("ok");
    }
}
