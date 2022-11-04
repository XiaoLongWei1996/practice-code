package com.springcloud.test.alibabaconfig.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 肖龙威
 * @date 2022/11/04 13:29
 */
@FeignClient(value = "consumer", fallback = ConsumerApiImpl.class)
public interface ConsumerApi {

    @GetMapping(value = "/test/read", headers = {"origin=app1"})
    String read();

}

@Component
class ConsumerApiImpl implements ConsumerApi {
    @Override
    public String read() {
        return "请求失败";
    }
}
