package com.springcloud.test.system;

import com.springcloud.test.system.config.feign.HomeApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SystemApplicationTests {

    @Autowired
    private HomeApi homeApi;

    @Test
    void contextLoads() {
        for (int i = 0; i < 10; i++) {
            homeApi.getToken();
        }

    }

}
