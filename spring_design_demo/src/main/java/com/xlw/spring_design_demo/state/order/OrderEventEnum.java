package com.xlw.spring_design_demo.state.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 订单事件
 * @Title: OrderEventEnum
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state
 * @Date 2025/1/17 16:08
 */
@AllArgsConstructor
@Getter
public enum OrderEventEnum {

    PAY("00", "交易"),

    SUCCESS("01", "支付成功"),

    FAIL("02", "支付失败"),

    REFUND("03", "退款");

    private String code;

    private String desc;
}
