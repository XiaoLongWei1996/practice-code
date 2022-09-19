package com.springcloud.test.system.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 肖龙威
 * @date 2022/09/16 14:36
 */
@FeignClient(value = "home")
public interface HomeApi {

    @GetMapping("/tool/acquireToken")
    String getToken();
}
