package com.springcloud.test.alibabaconfig.util;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @author 肖龙威
 * @date 2022/11/04 9:31
 */
@Component
public class HandleExceptionUtil {

    public static ClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException e) {
        System.out.println("执行");
        e.printStackTrace();
        return new SentinelClientHttpResponse("服务器繁忙");
    }
}
