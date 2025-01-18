package com.xlw.spring_design_demo.state.invoice;


import com.xlw.spring_design_demo.entity.Invoice;
import com.xlw.spring_design_demo.entity.Order;
import com.xlw.spring_design_demo.state.order.OrderEventEnum;
import com.xlw.spring_design_demo.state.order.OrderStateEnum;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @Title: InvoiceStateHandler
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state.invoice
 * @Date 2025/1/18 11:03
 */
@Component
@WithStateMachine(id = "invoiceMachine")
public class InvoiceStateHandler {

    @OnTransition(source = "WAIT_KP", target = "KPING")
    public void kp(Message<InvoiceEventEnum> message) {
        System.out.println("发票开具");
        InvoiceEventEnum payload = message.getPayload();
        Invoice invoice = (Invoice) message.getHeaders().get("data");
        invoice.setState(InvoiceStateEnum.KPING);
        System.out.println(payload + ":" + invoice);
    }

    @OnTransition(source = "KPING", target = "KP_SUCCESS")
    public void success(Message<InvoiceEventEnum> message) {
        System.out.println("开票成功");
        InvoiceEventEnum payload = message.getPayload();
        Invoice invoice = (Invoice) message.getHeaders().get("data");
        invoice.setState(InvoiceStateEnum.KP_SUCCESS);
        System.out.println(payload + ":" + invoice);
    }

    @OnTransition(source = "KP_SUCCESS", target = "KP_REFUND")
    public void hc(Message<InvoiceEventEnum> message) {
        System.out.println("红冲");
        InvoiceEventEnum payload = message.getPayload();
        Invoice invoice = (Invoice) message.getHeaders().get("data");
        invoice.setState(InvoiceStateEnum.KP_REFUND);
        System.out.println(payload + ":" + invoice);
    }
}
