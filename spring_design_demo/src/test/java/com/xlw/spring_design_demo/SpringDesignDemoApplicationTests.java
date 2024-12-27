package com.xlw.spring_design_demo;

import com.xlw.spring_design_demo.listener.event.ReadEvent;
import com.xlw.spring_design_demo.util.EventPublishUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringDesignDemoApplicationTests {

    @Resource
    private EventPublishUtil eventPublishUtil;

    @Test
    void monitor() {
        eventPublishUtil.publishEvent(ReadEvent.create("测试监听设计模式"));
    }

}
