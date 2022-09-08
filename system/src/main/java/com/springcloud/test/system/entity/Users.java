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
 * 用户表(Users)表实体类
 *
 * @author xlw
 * @since 2022-09-08 15:02:46
 */
@ApiModel("用户")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {
    
    /**
    * id
    */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
    * 名字
    */
    @ApiModelProperty(value = "名字")
    private String name;
    
    /**
    * 用户名称
    */
    @ApiModelProperty(value = "用户名称")
    private String userName;
    
    /**
    * 密码
    */
    @ApiModelProperty(value = "密码")
    private String password;
    
    /**
    * 加盐加密
    */
    @ApiModelProperty(value = "加盐加密")
    private String salt;
    
    /**
    * 生日
    */
    @ApiModelProperty(value = "生日")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date birth;
    
    /**
    * 下线时间
    */
    @ApiModelProperty(value = "下线时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date logoutDt;
    
    /**
    * 登录状态:0未登录, 1已登录,2已锁定
    */
    @ApiModelProperty(value = "登录状态:0未登录, 1已登录,2已锁定")
    private Integer state;
    
    /**
    * 登录时间
    */
    @ApiModelProperty(value = "登录时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loginDt;
    
    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDt;
    
    /**
    * 修改时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateDt;

    /**
     * 是否删除0否1是
     */
    @TableLogic
    @ApiModelProperty(value = "是否删除0否1是")
    private Integer isDelete;
}

