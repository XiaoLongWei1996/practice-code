package org.test.design.single;

/**
 * 饿汉式
 * @author 肖龙威
 * @date 2021/04/19 13:30
 */
public class Hunger {

    private static final Hunger HUNGER = new Hunger();

    private Hunger(){

    }

    public static Hunger getInstance(){
        return HUNGER;
    }
}
