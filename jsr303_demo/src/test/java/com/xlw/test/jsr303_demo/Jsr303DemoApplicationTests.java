package com.xlw.test.jsr303_demo;

import com.xlw.test.jsr303_demo.util.ValidatorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Jsr303DemoApplicationTests {

    @Test
    void contextLoads() {
        User user = new User();
        //user.setName("zhangsan");
        ValidatorUtil.validate(user);
    }

}
