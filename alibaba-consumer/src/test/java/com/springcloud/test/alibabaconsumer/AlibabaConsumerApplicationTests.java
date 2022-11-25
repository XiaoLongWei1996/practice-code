package com.springcloud.test.alibabaconsumer;

import com.springcloud.test.alibabaconsumer.dao.UsersMapper;
import com.springcloud.test.alibabaconsumer.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AlibabaConsumerApplicationTests {

    @Resource
    private UsersMapper usersMapper;

    @Test
    void contextLoads() {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("id", 1);
        payload.put("name", "李白");
        String token = TokenUtil.createToken(payload);

        System.out.println(token);

        boolean b = TokenUtil.validateToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi5p2O55m9IiwiaWQiOjEsIm5iZiI6MTY2OTM4OTA2MCwiZXhwIjoxNjY5Mzg5OTYwfQ.iejGkTKREV4ZvAleUmwFWDWpZ4G57rU0DHu6VPmZuFdordmPp-uXFoFT_4SwWt8npaPmNQM43KPydzgNoqBsBg");
        System.out.println(b);

        Map map = TokenUtil.parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi5p2O55m9IiwiaWQiOjEsIm5iZiI6MTY2OTM4OTA2MCwiZXhwIjoxNjY5Mzg5OTYwfQ.iejGkTKREV4ZvAleUmwFWDWpZ4G57rU0DHu6VPmZuFdordmPp-uXFoFT_4SwWt8npaPmNQM43KPydzgNoqBsBg"
                , Map.class);
        System.out.println(map);
    }

}
