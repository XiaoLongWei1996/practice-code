package org.test.design.decorator;

/**
 * @author 肖龙威
 * @date 2022/06/20 10:53
 */
public class Test {

    public static void main(String[] args) {
        Milk milk = new Milk(10,null);

        Kole kole = new Kole(3, milk);

        Water water = new Water(2, kole);

        Integer price = water.allPrice();
        System.out.println(price);
    }
}
