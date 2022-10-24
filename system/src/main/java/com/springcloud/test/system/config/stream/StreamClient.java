package com.springcloud.test.system.config.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author 肖龙威
 * @date 2022/10/24 17:05
 */
public interface StreamClient {

    /**
     * 输出通道
     */
    String OUTPUT = "myOutput";

    @Output()
    MessageChannel output();
}
