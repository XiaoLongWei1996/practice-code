package org.test.design.decorator;

/**
 * @author 肖龙威
 * @date 2021/04/29 15:55
 */
public class Water extends Drink{

    public Water(Integer price, Drink drink) {
        super(price, drink);
    }
}
