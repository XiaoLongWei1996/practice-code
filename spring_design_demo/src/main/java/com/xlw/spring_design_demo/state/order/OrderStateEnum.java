package com.xlw.spring_design_demo.state.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 订单状态
 * @Title: OrderStateEnum
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state
 * @Date 2025/1/17 15:37
 */
@Getter
@AllArgsConstructor
public enum OrderStateEnum {

    UNPAID("00", "待支付"),
    PAIDING("01", "支付中"),
    PAID_SUCCESS("02", "支付成功"),
    PAID_FAIL("03", "支付失败"),
    RETURN("04", "退款");

    private String code;

    private String desc;
}
