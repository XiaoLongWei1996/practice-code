package com.xlw.spring_design_demo.listener.event;


import org.springframework.context.ApplicationEvent;

/**
 * @description: 读事件
 * @Title: ReadEvent
 * @Author xlw
 * @Package com.xlw.spring_design_demo.listener.event
 * @Date 2024/12/18 16:24
 */
public class ReadEvent extends ApplicationEvent {

    public ReadEvent(String content) {
        super(content);
    }

    public static ReadEvent create(String content) {
        return new ReadEvent(content);
    }
}
