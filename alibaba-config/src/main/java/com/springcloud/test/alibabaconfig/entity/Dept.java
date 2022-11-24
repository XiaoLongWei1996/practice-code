package com.springcloud.test.alibabaconfig.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 部门
 *
 * @author 70492
 * @date 2022/11/24
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("dept")
public class Dept {


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 名字 */
    private String name;
    
    /**
    * 编码
    */
    private String code;
    
    /**
    * 类型0本部 1外部
    */
    private Integer type;
    
    /**
    * 是否删除0否1是
    */
    @TableLogic
    private Integer isDelete;
    
    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDt;
    
    /**
    * 修改时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateDt;
}

