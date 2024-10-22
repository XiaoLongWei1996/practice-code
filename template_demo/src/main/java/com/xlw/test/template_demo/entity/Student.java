package com.xlw.test.template_demo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
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

    @NotBlank
    private String name;

    private LocalDateTime birthday;
}
