package com.xlw.test.template_demo.util;


import com.xlw.test.template_demo.cons.TaskNotReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 重试工具类
 * @Title: RetryUtil
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2024/10/22 16:34
 */
@Slf4j
@Component
public class RetryUtil {


    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public void retry10(TaskNotReturn task) {
        task.execute();
    }

    @Retryable(value = RuntimeException.class, maxAttempts = 50, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public void retry50(TaskNotReturn task) {
        task.execute();
    }

    /**
     * 重试最大次数补偿
     *
     * @param e e
     */
    @Recover
    public void recover(RuntimeException e) {
        e.printStackTrace();
    }
}
