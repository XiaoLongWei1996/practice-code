package com.xlw.test.redis_cache_test.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抢票关联表(UserTicket)表实体类
 *
 * @author xlw
 * @since 2024-01-22 21:12:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user_ticket")
public class UserTicket {
    
    //用户id    
    @TableField(value = "user_id")
    private Integer userId;
    
    //票id    
    @TableField(value = "ticket_id")
    private Integer ticketId;

}
