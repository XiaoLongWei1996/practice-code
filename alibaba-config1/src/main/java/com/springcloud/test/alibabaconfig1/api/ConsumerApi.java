package com.springcloud.test.alibabaconfig1.api;

import feign.HeaderMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author 肖龙威
 * @date 2022/11/04 15:30
 */
@FeignClient(value = "consumer")
public interface ConsumerApi {

    @GetMapping(value = "/test/read")
    String read(@HeaderMap Map<String, Object> headers);
}

//@Component
//class ConsumerApiImpl implements ConsumerApi {
//    @Override
//    public String read() {
//        return "请求失败";
//    }
//}