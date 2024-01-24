package com.xlw.test.log4j2_test.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @Title: Student
 * @Author xlw
 * @Package com.xlw.test.log4j2_test.entity
 * @Date 2023/9/21 17:53
 */
@Data
public class Student {

    private Integer id;

    private String name;

    private Date birthday;
}
