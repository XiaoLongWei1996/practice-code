package com.springcloud.test.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 部门 表(Dept)表实体类
 *
 * @author xlw
 * @since 2022-09-06 10:36:58
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("部门")
@TableName("dept")
public class Dept {
    
    /**
    * 主键id
    */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
    
    /**
    * 名称
    */
    @ApiModelProperty("名称")
    private String name;
    
    /**
    * 编码
    */
    @ApiModelProperty("编码")
    private String code;
    
    /**
    * 类型0本部 1外部
    */
    @ApiModelProperty("类型,0本部 1外部")
    private Integer type;
    
    /**
    * 是否删除0否1是
    */
    @TableLogic
    @ApiModelProperty(value = "是否删除0否1是")
    private Integer isDelete;
    
    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDt;
    
    /**
    * 修改时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateDt;
}

