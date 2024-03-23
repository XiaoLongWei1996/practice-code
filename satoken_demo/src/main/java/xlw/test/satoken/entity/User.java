package xlw.test.satoken.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表(User)表实体类
 *
 * @author xlw
 * @since 2024-03-23 15:25:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("用户表实体类")
@TableName("user")
public class User {
    
  //主键    
  @ApiModelProperty(value = "主键", required = true, dataType = "Integer")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    
  //名字    
  @ApiModelProperty(value = "名字", required = true, dataType = "String")
  @TableField(value = "name")
  private String name;
    
  //年龄    
  @ApiModelProperty(value = "年龄", required = true, dataType = "Integer")
  @TableField(value = "age")
  private Integer age;
    
  //号码    
  @ApiModelProperty(value = "号码", required = true, dataType = "String")
  @TableField(value = "phone")
  private String phone;
    
  //用户名    
  @ApiModelProperty(value = "用户名", required = true, dataType = "String")
  @TableField(value = "user_name")
  private String userName;
    
  //密码    
  @ApiModelProperty(value = "密码", required = true, dataType = "String")
  @TableField(value = "password")
  private String password;
    
  //登录状态（0：未登录，1：已登录）    
  @ApiModelProperty(value = "登录状态（0：未登录，1：已登录）", required = true, dataType = "Integer")
  @TableField(value = "status")
  private Integer status;
    
  //账号锁（0：未锁，1：锁）    
  @ApiModelProperty(value = "账号锁（0：未锁，1：锁）", required = true, dataType = "Integer")
  @TableField(value = "lock_accont")
  private Integer lockAccont;
    
  //最后一次登录时间    
  @ApiModelProperty(value = "最后一次登录时间", required = true, dataType = "Date")
  @TableField(value = "last_dt")
  private Date lastDt;
    
  //客户端ip    
  @ApiModelProperty(value = "客户端ip", required = true, dataType = "String")
  @TableField(value = "ip")
  private String ip;
    
  //账号是否被禁用（0：否，1：是）    
  @ApiModelProperty(value = "账号是否被禁用（0：否，1：是）", required = true, dataType = "Integer")
  @TableField(value = "disable")
  private Integer disable;
    
  //封禁日期    
  @ApiModelProperty(value = "封禁日期", required = true, dataType = "Date")
  @TableField(value = "disable_dt")
  private Date disableDt;
    
  //解封时间    
  @ApiModelProperty(value = "解封时间", required = true, dataType = "Date")
  @TableField(value = "enable_dt")
  private Date enableDt;
    
  //创建时间    
  @ApiModelProperty(value = "创建时间", required = true, dataType = "Date")
  @TableField(value = "create_dt")
  private Date createDt;
    
  //修改时间    
  @ApiModelProperty(value = "修改时间", required = true, dataType = "Date")
  @TableField(value = "update_dt")
  private Date updateDt;

}
