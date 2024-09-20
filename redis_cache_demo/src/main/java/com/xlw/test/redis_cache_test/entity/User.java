package com.xlw.test.redis_cache_test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户表(User)表实体类
 *
 * @author xlw
 * @since 2023-12-23 19:43:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User {
    
    //主键    
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    //名字    
    @TableField(value = "name")
    private String name;
    
    //年龄    
    @TableField(value = "age")
    private Integer age;
    
    //号码    
    @TableField(value = "phone")
    private String phone;
    
    //用户名    
    @TableField(value = "user_name")
    private String userName;
    
    //密码    
    @TableField(value = "password")
    private String password;
    
    //创建时间
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_dt")
    private LocalDateTime createDt;
    
    //修改时间    
    @TableField(value = "update_dt")
    private LocalDateTime updateDt;

}
