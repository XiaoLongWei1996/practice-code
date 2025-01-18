package com.xlw.spring_design_demo.strategy;


import org.springframework.stereotype.Component;

/**
 * @description: 淘宝订单策略
 * @Title: TaoBaoOrderTrategy
 * @Author xlw
 * @Package com.xlw.spring_design_demo.strategy
 * @Date 2025/1/17 14:43
 */
@Component("taoBaoOrderTrategy")
public class TaoBaoOrderTrategy implements OrdersTrategy {

    @Override
    public void doSomething() {
        System.out.println("淘宝订单策略");
    }
}
