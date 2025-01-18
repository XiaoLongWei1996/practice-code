package com.xlw.spring_design_demo.entity;


import com.xlw.spring_design_demo.state.order.OrderStateEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @description: 订单
 * @Title: Order
 * @Author xlw
 * @Package com.xlw.spring_design_demo.entity
 * @Date 2025/1/17 16:51
 */
@Data
public class Order {

    private String ddid;

    private OrderStateEnum ddzt;

    private BigDecimal price;

    private LocalDateTime createDate;

}
