package org.xlw.test.shardingspherejdbc_demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Dept0)表实体类
 *
 * @author xlw
 * @since 2024-03-14 15:16:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dept")
public class Dept {
    
  //主键    
  @TableId(value = "id")
  private Long id;
    
  //userId    
  @TableField(value = "user_id")
  private Integer userId;
    
  //部门名称    
  @TableField(value = "name")
  private String name;
    
  //创建时间    
  @TableField(value = "create_dt")
  private Date createDt;
    
  //修改时间    
  @TableField(value = "update_dt")
  private Date updateDt;

}
