package com.xlw.spring_design_demo.state.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 发票状态
 * @Title: InvoiceStateEnum
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state.invoice
 * @Date 2025/1/18 10:43
 */
@AllArgsConstructor
@Getter
public enum InvoiceStateEnum {

    WAIT_KP("00", "待开票"),

    KPING("01", "开票中"),

    KP_SUCCESS("02", "开票成功"),

    KP_FAIL("03", "开票失败"),

    KP_REFUND("04", "开票退款")

    ;

    private String code;

    private String desc;
}
