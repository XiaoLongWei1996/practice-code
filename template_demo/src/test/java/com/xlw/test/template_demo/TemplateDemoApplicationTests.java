package com.xlw.test.template_demo;

import com.xlw.test.template_demo.exception.BusinessException;
import com.xlw.test.template_demo.extend.retry.RetryTask;
import com.xlw.test.template_demo.util.IdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest(classes = TemplateDemoApplication.class)
class TemplateDemoApplicationTests {

    @Resource
    private RetryTask retryTask;

    @Resource
    private ThreadPoolTaskExecutor executor;

    @Test
    void contextLoads() {
        retryTask.retryTask1(() -> {
            System.out.println("执行");
            throw new BusinessException("异常");
        });
    }

    @Test
    public void test() throws InterruptedException, IOException {
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String s = IdUtils.safeId("3AD5");
                System.out.println(s);
            });
            thread.start();
        }
        System.in.read();
    }

}
