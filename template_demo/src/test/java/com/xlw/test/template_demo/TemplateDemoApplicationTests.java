package com.xlw.test.template_demo;

import com.xlw.test.template_demo.exception.BusinessException;
import com.xlw.test.template_demo.extend.retry.RetryTask;
import com.xlw.test.template_demo.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest(classes = TemplateDemoApplication.class)
class TemplateDemoApplicationTests {

    @Resource
    private RetryTask retryTask;


    @Test
    void contextLoads() {
        retryTask.retryTask1(() -> {
            System.out.println("执行");
            throw new BusinessException("异常");
        });
    }

    @Test
    public void test() throws InterruptedException, IOException {
        Object hello = RedisUtil.get("hello");
        System.out.println(hello);
    }

}
