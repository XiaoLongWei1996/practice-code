package com.xlw.test.redis_cache_test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 优惠卷表(Ticket)表实体类
 *
 * @author xlw
 * @since 2024-01-22 21:12:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("ticket")
public class Ticket {
    
    //主键    
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    //卷名    
    @TableField(value = "name")
    private String name;
    
    //票数    
    @TableField(value = "ticket_count")
    private Integer ticketCount;
    
    //创建时间    
    @TableField(value = "create_dt")
    private Date createDt;
    
    //修改时间    
    @TableField(value = "update_dt")
    private Date updateDt;

}
