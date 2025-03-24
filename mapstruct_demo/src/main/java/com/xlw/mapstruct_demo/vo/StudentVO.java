package com.xlw.mapstruct_demo.vo;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xlw
 * @description:
 * @title: StudentVO
 * @package com.xlw.mapstruct_demo.vo
 * @date 2025/3/24 19:27
 */
@Data
public class StudentVO {

    private String name;

    private Integer age;

    private LocalDateTime birthday;

    private String msg;
}
