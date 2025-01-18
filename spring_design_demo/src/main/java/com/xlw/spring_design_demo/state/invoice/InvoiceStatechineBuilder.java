package com.xlw.spring_design_demo.state.invoice;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * @description: 发票状态机构建器
 * @Title: InvoiceStatechineBuilder
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state.invoice
 * @Date 2025/1/18 11:37
 */
@Component
public class InvoiceStatechineBuilder {

    @Bean
    public StateMachine<InvoiceStateEnum, InvoiceEventEnum> invoiceStateMachine(BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<InvoiceStateEnum, InvoiceEventEnum> builder = StateMachineBuilder.builder();
        // 配置状态机
        builder
                .configureConfiguration()
                .withConfiguration()
                .machineId("invoiceMachine")
                .beanFactory(beanFactory)
                .autoStartup(true);
        //配置状态
        builder.configureStates()
                .withStates()
                .initial(InvoiceStateEnum.WAIT_KP)
                .states(EnumSet.allOf(InvoiceStateEnum.class));
        //配置事件
        builder.configureTransitions()
                .withExternal().source(InvoiceStateEnum.WAIT_KP).target(InvoiceStateEnum.KPING).event(InvoiceEventEnum.KP)
                .and()
                .withExternal().source(InvoiceStateEnum.KPING).target(InvoiceStateEnum.KP_SUCCESS).event(InvoiceEventEnum.SUCCESS)
                .and()
                .withExternal().source(InvoiceStateEnum.KP_SUCCESS).target(InvoiceStateEnum.KP_REFUND).event(InvoiceEventEnum.HC);
        return builder.build();
    }
}
