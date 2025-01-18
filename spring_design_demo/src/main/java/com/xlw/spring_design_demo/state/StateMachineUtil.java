package com.xlw.spring_design_demo.state;


import com.xlw.spring_design_demo.util.SpringContextHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

/**
 * @description: 状态机工具类
 * @Title: StateMachineUtil
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state
 * @Date 2025/1/17 17:06
 */
public class StateMachineUtil {

    public static <E, T> boolean sendEvent(E event, T data, StateMachine stateMachine) {

        Message<E> message = MessageBuilder.withPayload(event).setHeader("data", data).build();
        return stateMachine.sendEvent(message);
    }
}
