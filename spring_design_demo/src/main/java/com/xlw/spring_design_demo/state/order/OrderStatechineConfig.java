package com.xlw.spring_design_demo.state.order;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.*;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * @description: 订单状态机配置类
 * @Title: OrderStatechineConfig
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state
 * @Date 2025/1/17 16:16
 */
//@Configuration
public class OrderStatechineConfig extends StateMachineConfigurerAdapter<OrderStateEnum, OrderEventEnum> {

    /**
     * 状态机额外配置
     *
     * @param config 配置
     * @throws Exception 例外
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStateEnum, OrderEventEnum> config) throws Exception {
        config
                .withConfiguration()
                .machineId("orderHandler")
                .autoStartup(true)
                .listener(listener());
    }

    /**
     * 状态配置
     *
     * @param states 国家
     * @throws Exception 例外
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderStateEnum, OrderEventEnum> states) throws Exception {
        states.withStates()
                .initial(OrderStateEnum.UNPAID)    //初始状态
                //.end(OrderStateEnum.PAID_SUCCESS)  //结束状态
                //.end(OrderStateEnum.PAID_FAIL)     //结束状态
                //.end(OrderStateEnum.RETURN)        //结束状态
                .states(EnumSet.allOf(OrderStateEnum.class));
    }

    /**
     * 配置状态转换事件关系
     *
     * @param transitions 转换
     * @throws Exception 例外
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStateEnum, OrderEventEnum> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStateEnum.UNPAID).target(OrderStateEnum.PAIDING).event(OrderEventEnum.PAY)
                .and()
                .withExternal().source(OrderStateEnum.PAIDING).target(OrderStateEnum.PAID_SUCCESS).event(OrderEventEnum.SUCCESS)
                .and()
                .withExternal().source(OrderStateEnum.PAIDING).target(OrderStateEnum.PAID_FAIL).event(OrderEventEnum.FAIL)
                .and()
                .withExternal().source(OrderStateEnum.PAID_SUCCESS).target(OrderStateEnum.RETURN).event(OrderEventEnum.REFUND);
    }

    /**
     * 监听器
     *
     * @return {@link StateMachineListener }<{@link OrderStateEnum }, {@link OrderEventEnum }>
     */
    @Bean
    public StateMachineListener<OrderStateEnum, OrderEventEnum> listener() {
        return new StateMachineListenerAdapter<OrderStateEnum, OrderEventEnum>() {
            @Override
            public void stateChanged(State<OrderStateEnum, OrderEventEnum> from, State<OrderStateEnum, OrderEventEnum> to) {
                System.out.println("状态改变:" + from.getId() + "->" + to.getId());
            }

            @Override
            public void eventNotAccepted(Message<OrderEventEnum> event) {
                System.out.println("状态流转不匹配:" + event.getPayload());
            }

            @Override
            public void stateMachineError(StateMachine<OrderStateEnum, OrderEventEnum> stateMachine, Exception exception) {
                System.out.println("状态机错误");
            }
        };
    }

}
