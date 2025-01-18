package com.xlw.spring_design_demo.state.order;


import com.xlw.spring_design_demo.entity.Order;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @description: 状态机处理器
 * @Title: OrderStateHandler
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state
 * @Date 2025/1/17 16:49
 */
@Component
@WithStateMachine(id = "orderMachine")
public class OrderStateHandler {

    @OnTransition(source = "UNPAID", target = "PAIDING")
    public void pay(Message<OrderEventEnum> message) {
        System.out.println("订单交易");
        OrderEventEnum payload = message.getPayload();
        Order order = (Order) message.getHeaders().get("data");
        order.setDdzt(OrderStateEnum.PAIDING);
        System.out.println(payload + ":" + order);
    }

    @OnTransition(source = "PAIDING", target = "PAID_SUCCESS")
    public void success(Message<OrderEventEnum> message) {
        System.out.println("支付成功");
        OrderEventEnum payload = message.getPayload();
        Order order = (Order) message.getHeaders().get("data");
        order.setDdzt(OrderStateEnum.PAID_SUCCESS);
        System.out.println(payload + ":" + order);
    }

    @OnTransition(source = "PAIDING", target = "PAID_FAIL")
    public void fail(Message<OrderEventEnum> message) {
        System.out.println("支付失败");
        OrderEventEnum payload = message.getPayload();
        Order order = (Order) message.getHeaders().get("data");
        order.setDdzt(OrderStateEnum.PAID_FAIL);
        System.out.println(payload + ":" + order);
    }

    @OnTransition(source = "PAID_SUCCESS", target = "RETURN")
    public void refund(Message<OrderEventEnum> message) {
        System.out.println("退款");
        OrderEventEnum payload = message.getPayload();
        Order order = (Order) message.getHeaders().get("data");
        order.setDdzt(OrderStateEnum.RETURN);
        System.out.println(payload + ":" + order);
    }
}
