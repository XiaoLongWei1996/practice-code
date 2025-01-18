package com.xlw.spring_design_demo.strategy;


import org.springframework.stereotype.Component;

/**
 * @description:
 * @Title: JDOrderTrategy
 * @Author xlw
 * @Package com.xlw.spring_design_demo.strategy
 * @Date 2025/1/17 14:44
 */
@Component("jdOrderTrategy")
public class JDOrderTrategy implements OrdersTrategy {

    @Override
    public void doSomething() {
        System.out.println("京东订单策略");
    }
}
