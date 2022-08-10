package com.test.springboot.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 定义事件源
 * @author 肖龙威
 * @date 2022/07/06 10:31
 */
public class MyEvent extends ApplicationEvent {

    public MyEvent(Object source) {
        super(source);
    }

}
