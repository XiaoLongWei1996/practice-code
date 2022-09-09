package com.springcloud.test.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色绑定(UserRole)表实体类
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@ApiModel("用户角色绑定")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRole {
    
    /**
    * id
    */
    @ApiModelProperty(value = "id")
    private Integer id;
    
    /**
    * 用户id
    */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    
    /**
    * 角色id
    */
    @ApiModelProperty(value = "角色id")
    private Integer roleId;
}

