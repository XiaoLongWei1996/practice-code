package com.xlw.test.template_demo;

import com.xlw.test.template_demo.exception.BusinessException;
import com.xlw.test.template_demo.extend.retry.RetryTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

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
    public void test() throws InterruptedException {
        executor.execute(() -> {
            retryTask.retryTask1(() -> {
                System.out.println("执行1");
                try {
                    throw new Exception();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        executor.execute(() -> {
            retryTask.retryTask2(() -> {
                System.out.println("执行2");
                try {
                    throw new Exception();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        Thread.sleep(100000);
    }

}
