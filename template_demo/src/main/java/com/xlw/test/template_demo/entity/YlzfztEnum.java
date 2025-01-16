package com.xlw.test.template_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 银联支付状态
 * @Title: YlzfztEnum
 * @Author xlw
 * @Package com.invoice.cons
 * @Date 2024/7/16 9:29
 */
@Getter
@AllArgsConstructor
public enum YlzfztEnum {

    WAITING("0", "待处理"),

    PROCESSING("1", "处理中"),

    SUBMITTED("2", "已提交"),

    SUCCESS("3", "处理成功"),

    FAIL("4", "处理失败"),

    UNKNOWN("7", "未知");
    private String code;

    private String desc;

}
