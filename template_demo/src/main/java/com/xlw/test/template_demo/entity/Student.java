package com.xlw.test.template_demo.entity;

import com.xlw.test.template_demo.util.DesensitizeUtil;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @Title: Student
 * @Author xlw
 * @Package com.sxkj.pay.entity
 * @Date 2024/10/9 15:30
 */
@Data
public class Student {

    @DesensitizeUtil.Desensitize(value = DesensitizeUtil.DesensitizeType.CUSTOMIZE, startInclude = 1, endExclude = 0)
    private String name;

    private LocalDateTime birthday;

    @DesensitizeUtil.Desensitize(DesensitizeUtil.DesensitizeType.ID_CARD)
    private String idcard;


}
