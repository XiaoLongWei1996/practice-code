package com.test.springboot.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/08/16 17:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hero")
public class Hero {

    @TableId(value = "id")
    private Long id;

    private String name;

    private Integer power;

    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateDt;


    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

    private Long classId;

    @TableField(exist = false) //设置冗余字段
    private String className;

}
