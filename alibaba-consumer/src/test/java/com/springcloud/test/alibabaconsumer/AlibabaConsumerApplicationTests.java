package com.springcloud.test.alibabaconsumer;

import cn.hutool.crypto.digest.DigestUtil;
import com.springcloud.test.alibabaconsumer.dao.UsersMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AlibabaConsumerApplicationTests {

    @Resource
    private UsersMapper usersMapper;

    @Test
    void contextLoads() {
        String pwd = DigestUtil.md5Hex("123456");
        System.out.println(new String(pwd));
    }

}
