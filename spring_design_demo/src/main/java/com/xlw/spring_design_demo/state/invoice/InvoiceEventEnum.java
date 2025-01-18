package com.xlw.spring_design_demo.state.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 发票事件
 * @Title: InvoiceEventEnum
 * @Author xlw
 * @Package com.xlw.spring_design_demo.state.invoice
 * @Date 2025/1/18 10:44
 */
@AllArgsConstructor
@Getter
public enum InvoiceEventEnum {

    KP("00", "开票"),

    HC("01", "红冲"),

    SUCCESS("02", "开票成功"),

    FAIL("03", "开票失败"),

    ;

    private String code;

    private String desc;
}
