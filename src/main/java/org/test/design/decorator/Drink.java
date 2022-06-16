package org.test.design.decorator;

import lombok.AllArgsConstructor;

/**
 * 装饰者设计模式:在不改变对象原有的结构上增加代码功能
 * @author 肖龙威
 * @date 2021/04/29 15:39
 */
@AllArgsConstructor
public abstract class Drink {

    protected Integer price;

    protected Drink drink;

    public Integer allPrice(){
        if (drink == null){
            return price;
        }
        return price + drink.allPrice();
    }

}
