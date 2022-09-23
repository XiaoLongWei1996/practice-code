package com.springcloud.test.system.config.feign;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 肖龙威
 * @date 2022/09/16 14:36
 */
@FeignClient(value = "home", fallback = FailMethod.class)
//@Retry(name = "retryA", fallbackMethod = "retryFail")
@TimeLimiter(name = "timeA")
public interface HomeApi {


    @GetMapping("/tool/acquireToken")
    String getToken();

    default String retryFail(Exception e) throws Exception {
        throw e;
        //return "重试请求失败";
    }
}
