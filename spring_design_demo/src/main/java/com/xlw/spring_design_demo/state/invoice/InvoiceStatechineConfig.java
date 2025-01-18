package com.xlw.spring_design_demo.state.invoice;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * @description:
 * @Title: InvoiceStatechineConfig
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state.invoice
 * @Date 2025/1/18 10:46
 */
//@Configuration
public class InvoiceStatechineConfig extends StateMachineConfigurerAdapter<InvoiceStateEnum, InvoiceEventEnum> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<InvoiceStateEnum, InvoiceEventEnum> config) throws Exception {
        config
                .withConfiguration()
                .machineId("invoiceHandler")
                .autoStartup(true)
                .listener(listener1());
    }

    @Override
    public void configure(StateMachineStateConfigurer<InvoiceStateEnum, InvoiceEventEnum> states) throws Exception {
        states.withStates()
                .initial(InvoiceStateEnum.WAIT_KP)
                .states(EnumSet.allOf(InvoiceStateEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<InvoiceStateEnum, InvoiceEventEnum> transitions) throws Exception {
        transitions
                .withExternal().source(InvoiceStateEnum.WAIT_KP).target(InvoiceStateEnum.KPING).event(InvoiceEventEnum.KP)
                .and()
                .withExternal().source(InvoiceStateEnum.KPING).target(InvoiceStateEnum.KP_SUCCESS).event(InvoiceEventEnum.SUCCESS)
                .and()
                .withExternal().source(InvoiceStateEnum.KP_SUCCESS).target(InvoiceStateEnum.KP_REFUND).event(InvoiceEventEnum.HC);
    }

    @Bean
    public StateMachineListener<InvoiceStateEnum, InvoiceEventEnum> listener1() {
        return new StateMachineListenerAdapter<InvoiceStateEnum, InvoiceEventEnum>() {
            @Override
            public void stateChanged(State<InvoiceStateEnum, InvoiceEventEnum> from, State<InvoiceStateEnum, InvoiceEventEnum> to) {
                System.out.println("状态改变:" + from.getId() + "->" + to.getId());
            }


            @Override
            public void eventNotAccepted(Message<InvoiceEventEnum> event) {
                System.out.println("状态流转不匹配:" + event.getPayload());
            }

            @Override
            public void stateMachineError(StateMachine<InvoiceStateEnum, InvoiceEventEnum> stateMachine, Exception exception) {
                System.out.println("状态机错误");
            }
        };
    }
}
