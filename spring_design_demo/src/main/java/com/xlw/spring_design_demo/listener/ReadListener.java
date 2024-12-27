package com.xlw.spring_design_demo.listener;


import com.xlw.spring_design_demo.listener.event.ReadEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @description: 读监听器
 * @Title: ReadListener
 * @Author xlw
 * @Package com.xlw.spring_design_demo.listener
 * @Date 2024/12/18 16:26
 */
@Component
public class ReadListener implements ApplicationListener<ReadEvent> {

    @Override
    public void onApplicationEvent(ReadEvent event) {
        String content = (String) event.getSource();
        System.out.println("监听结果：" + content);
    }
}
