package com.springcloud.test.alibabaconsumer.util;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Component;

/**
 * @author 肖龙威
 * @date 2022/11/02 14:54
 */
@Component
public class SendUtil {

    @SentinelResource(value = "recieve", fallback = "error")
    public String recieve() {
        return "接收";
    }

    public String error() {
        return "请求出错";
    }
}
