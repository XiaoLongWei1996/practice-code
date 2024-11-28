package com.xlw.spring_design_demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @description: 事件发布工具类
 * @Title: EventPublishUtil
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2024/8/12 17:47
 */
@RequiredArgsConstructor
@Component
public class EventPublishUtil {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发布事件
     *
     * @param event 事件
     */
    public void publishEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

}
