package com.springcloud.test.alibabaconfig1;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.springcloud.test.alibabaconfig1.dao.DeptMapper;
import com.springcloud.test.alibabaconfig1.entity.Dept;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class AlibabaConfig1ApplicationTests {

    @Resource
    private DeptMapper deptMapper;

    @Test
    void contextLoads() {
        List<Dept> depts = deptMapper.selectList(Wrappers.emptyWrapper());
        System.out.println(depts);
    }

}
