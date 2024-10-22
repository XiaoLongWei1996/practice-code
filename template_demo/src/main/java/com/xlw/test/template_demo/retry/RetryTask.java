package com.xlw.test.template_demo.retry;

import com.xlw.test.template_demo.exception.BusinessException;
import com.xlw.test.template_demo.exception.ServiceException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @description:
 * @Title: RetryTask
 * @Author xlw
 * @Package com.xlw.test.template_demo.retry
 * @Date 2024/10/22 11:41
 */
@Component
public class RetryTask {

    @Retryable(value = BusinessException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public void retryTask1(Supplier supplier) {
        Object o = supplier.get();
        System.out.println("执行重试任务" + o);
    }

    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public void retryTask2(Supplier supplier) {
        Object o = supplier.get();
        System.out.println("执行重试任务" + o);
    }

    @Recover
    public void recover1(BusinessException e) {
        e.printStackTrace();
        System.out.println("recover1执行");
    }

    @Recover
    public void recover2(Exception e) {
        e.printStackTrace();
        System.out.println("recover2执行");
    }
}
