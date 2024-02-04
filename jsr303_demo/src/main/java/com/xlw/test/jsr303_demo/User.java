package com.xlw.test.jsr303_demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xlw.test.jsr303_demo.config.ValidGroup;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * @description:
 * @Title: User
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo
 * @Date 2024/2/3 15:56
 */
@Data
public class User {

    @NotNull()
    @Max(value = 10, groups = ValidGroup.Insert.class) //分组
    @Min(value = 5, groups = ValidGroup.Update.class)
    private Integer id;

    @NotEmpty
    private String name;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Past
    private LocalDateTime birthday;

    @Pattern(message = "号码格式错误", regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$")
    private String phone;

}
