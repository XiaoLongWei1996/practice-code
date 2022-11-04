package com.springcloud.test.alibabaconfig1.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author 肖龙威
 * @date 2022/11/04 15:30
 */
@FeignClient(value = "consumer", fallback = ConsumerApiImpl.class)
public interface ConsumerApi {

    @GetMapping(value = "/test/read")
    String read(@RequestHeader("origin") String origin);
}

@Component
class ConsumerApiImpl implements ConsumerApi {
    @Override
    public String read(String origin) {
        return "服务器繁忙";
    }
}