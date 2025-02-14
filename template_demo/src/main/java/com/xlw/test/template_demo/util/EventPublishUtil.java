package com.xlw.test.template_demo.util;

import org.springframework.context.ApplicationEvent;

/**
 * @description: 事件发布工具类
 * @Title: EventPublishUtil
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2024/8/12 17:47
 */
public class EventPublishUtil {

    /**
     * 发布事件
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        ApplicationContextUtil.publishEvent(event);
    }

}
