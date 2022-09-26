package com.springcloud.test.system.config.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.CompletableFuture;

/**
 * @author 肖龙威
 * @date 2022/09/16 14:36
 */
@Component
@FeignClient(value = "home", path = "/tool")
//@TimeLimiter(name = "timeA")
//@Retry(name = "retryA", fallbackMethod = "retryFail")
//@RateLimiter(name = "rateA", fallbackMethod = "rateFail")
@CircuitBreaker(name = "cirA", fallbackMethod = "rateFail")
//@Bulkhead(name = "bluB", type = Bulkhead.Type.THREADPOOL)
public interface HomeApi {


    @GetMapping("/acquireToken")
    String getToken();

    @GetMapping("/acquireToken")
    CompletableFuture<String> getToken01();


    default String retryFail(Exception e) throws Exception {
        e.printStackTrace();
        return "error";
    }

    default String rateFail(Exception e) throws Exception {
        throw e;
        //return "超过访问限制";
    }
}
