package com.springcloud.test.alibabaconfig;

import com.springcloud.test.alibabaconfig.dao.DeptMapper;
import com.springcloud.test.alibabaconfig.entity.Dept;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class AlibabaConfigApplicationTests {

    @Resource
    private DeptMapper deptMapper;

    @Test
    void contextLoads() {
        List<Dept> depts = deptMapper.selectList(null);
        System.out.println(depts);
    }

}
