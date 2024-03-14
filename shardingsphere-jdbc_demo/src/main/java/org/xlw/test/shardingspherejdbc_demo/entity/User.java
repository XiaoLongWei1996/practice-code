package org.xlw.test.shardingspherejdbc_demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表(User)表实体类
 *
 * @author xlw
 * @since 2024-03-14 16:45:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
  @TableField(value = "create_dt")
  private Date createDt;
    
  //修改时间    
  @TableField(value = "update_dt")
  private Date updateDt;

}
