package com.xlw.kafka_demo.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Title: Student
 * @Author xlw
 * @Package com.xlw.kafka_demo.entity
 * @Date 2023/6/25 9:39
 * @description: 学生类
 */
@Data
public class Student {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotBlank
    @Length(min = 2, message = "名字最小两个字")
    private String name;

    @NotNull
    private Integer age;
}
