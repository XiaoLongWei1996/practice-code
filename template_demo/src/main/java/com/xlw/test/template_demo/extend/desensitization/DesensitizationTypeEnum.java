package com.xlw.test.template_demo.extend.desensitization;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * desensitization 类型枚举
 *
 * @author xlw
 * @date 2024/10/31
 */
@AllArgsConstructor
@Getter
public enum DesensitizationTypeEnum {

    CUSTOM_RULE("自定义脱敏规则"),

    ID_CARD("身份证"),

    MOBILE("手机号"),

    BANK_CARD("银行卡"),

    NAME("姓名"),

    ;
    private String msg;
}
