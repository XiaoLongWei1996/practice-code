package com.xlw.mapstruct_demo.entity;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xlw
 * @description:
 * @title: Student
 * @package com.xlw.mapstruct_demo.entity
 * @date 2025/3/24 19:27
 */
@Data
public class Student {

    private String name;

    private Integer age;

    private LocalDateTime birthday;

    private String desc;
}
