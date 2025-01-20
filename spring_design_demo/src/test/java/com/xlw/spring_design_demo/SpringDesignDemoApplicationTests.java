package com.xlw.spring_design_demo;

import com.xlw.spring_design_demo.entity.Invoice;
import com.xlw.spring_design_demo.entity.Order;
import com.xlw.spring_design_demo.listener.event.ReadEvent;
import com.xlw.spring_design_demo.state.invoice.InvoiceEventEnum;
import com.xlw.spring_design_demo.state.invoice.InvoiceStateEnum;
import com.xlw.spring_design_demo.state.order.OrderEventEnum;
import com.xlw.spring_design_demo.state.order.OrderStateEnum;
import com.xlw.spring_design_demo.state.StateMachineUtil;
import com.xlw.spring_design_demo.strategy.OrdersTrategy;
import com.xlw.spring_design_demo.util.EventPublishUtil;
import com.xlw.spring_design_demo.util.SpringContextHelper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.awt.image.Kernel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
class SpringDesignDemoApplicationTests {

    @Resource
    private StateMachine<OrderStateEnum, OrderEventEnum> orderStateMachine;

    @Resource
    private StateMachine<InvoiceStateEnum, InvoiceEventEnum> invoiceStateMachine;

    @Test
    void monitor() {
        EventPublishUtil.publishEvent(ReadEvent.create("测试监听设计模式"));
    }

    @Test
    void trategy() {
        Map<String, OrdersTrategy> map = SpringContextHelper.getBeansOfType(OrdersTrategy.class);
        System.out.println(map);
    }

    @Test
    void stateMachine() {
        Order order = new Order();
        order.setDdid("1");
        order.setDdzt(OrderStateEnum.UNPAID);
        order.setPrice(BigDecimal.valueOf(1000));
        order.setCreateDate(LocalDateTime.now());
        StateMachineUtil.sendEvent(OrderEventEnum.PAY, order, orderStateMachine);
        System.out.println(order);
        StateMachineUtil.sendEvent(OrderEventEnum.SUCCESS, order, orderStateMachine);
        System.out.println(order);
        StateMachineUtil.sendEvent(OrderEventEnum.REFUND, order, orderStateMachine);
        System.out.println(order);
    }

    @Test
    void stateMachine2() {
        Invoice invoice = new Invoice();
        invoice.setUuid("12312");
        invoice.setState(InvoiceStateEnum.WAIT_KP);
        StateMachineUtil.sendEvent(InvoiceEventEnum.KP, invoice, invoiceStateMachine);
        System.out.println(invoice);
    }

    @Test
    void test() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.replaceAll((k, v) -> v + 1);
        System.out.println(map);
    }
}
