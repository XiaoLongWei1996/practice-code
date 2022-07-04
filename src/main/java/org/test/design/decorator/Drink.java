package org.test.design.decorator;

/**
 * 装饰者设计模式:在不改变对象原有的结构上增加代码功能
 * 也就是把一个对象作为另一个对象的属性,层层包含
 * 这样一个对象就可以具有多个对象的功能
 * @author 肖龙威
 * @date 2021/04/29 15:39
 */
public abstract class Drink {

    protected Integer price;

    protected Drink drink;

    public Drink(Integer price, Drink drink) {
        this.price = price;
        this.drink = drink;
    }

    public Integer allPrice(){
        if (drink == null){
            return price;
        }
        return price + drink.allPrice();
    }

}
