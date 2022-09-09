package com.springcloud.test.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色权限绑定(RolePerm)表实体类
 *
 * @author xlw
 * @since 2022-09-09 14:24:23
 */
@ApiModel("角色权限绑定")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolePerm {
    
    /**
    * id
    */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
    * 角色id
    */
    @ApiModelProperty(value = "角色id")
    private Integer roleId;
    
    /**
    * 权限id
    */
    @ApiModelProperty(value = "权限id")
    private Integer permissionsId;
}

