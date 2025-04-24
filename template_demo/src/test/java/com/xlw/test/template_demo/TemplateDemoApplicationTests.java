package com.xlw.test.template_demo;

import com.xlw.test.template_demo.exception.BusinessException;
import com.xlw.test.template_demo.extend.retry.RetryTask;
import com.xlw.test.template_demo.task.ForkJoinTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

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
    public void test() throws InterruptedException, IOException, ExecutionException {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        ForkJoinPool pool = new ForkJoinPool(3);
        ForkJoinTask task = new ForkJoinTask(list, 10, 0, list.size());
        pool.submit(task);
        List<Integer> integers = task.get();
        System.out.println(integers);
    }

}
