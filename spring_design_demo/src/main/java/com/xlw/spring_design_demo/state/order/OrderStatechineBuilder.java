package com.xlw.spring_design_demo.state.order;


import com.xlw.spring_design_demo.state.invoice.InvoiceEventEnum;
import com.xlw.spring_design_demo.state.invoice.InvoiceStateEnum;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * @description: 订单状态机构建器
 * @Title: OrderStatechineBuilder
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state.order
 * @Date 2025/1/18 11:46
 */
@Component
public class OrderStatechineBuilder {

    @Bean
    public StateMachine<OrderStateEnum, OrderEventEnum> orderStateMachine(BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<OrderStateEnum, OrderEventEnum> builder = StateMachineBuilder.builder();
        // 配置状态机
        builder.configureConfiguration()
                .withConfiguration()
                .machineId("orderMachine")
                .beanFactory(beanFactory)
                .autoStartup(true);
        //配置状态
        builder.configureStates()
                .withStates()
                .initial(OrderStateEnum.UNPAID)    //初始状态
                .states(EnumSet.allOf(OrderStateEnum.class));
        //配置事件
        builder.configureTransitions()
                .withExternal().source(OrderStateEnum.UNPAID).target(OrderStateEnum.PAIDING).event(OrderEventEnum.PAY)
                .and()
                .withExternal().source(OrderStateEnum.PAIDING).target(OrderStateEnum.PAID_SUCCESS).event(OrderEventEnum.SUCCESS)
                .and()
                .withExternal().source(OrderStateEnum.PAIDING).target(OrderStateEnum.PAID_FAIL).event(OrderEventEnum.FAIL)
                .and()
                .withExternal().source(OrderStateEnum.PAID_SUCCESS).target(OrderStateEnum.RETURN).event(OrderEventEnum.REFUND);
        return builder.build();
    }
}
