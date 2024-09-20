package com.xlw.test.xxl_job_test.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @Title: TestJob
 * @Author xlw
 * @Package com.xlw.test.xxl_job_test.job
 * @Date 2023/9/22 20:15
 */
@Component
public class TestJob {

    @XxlJob("test")
    public void test() {
        System.out.println("执行");
    }
}
