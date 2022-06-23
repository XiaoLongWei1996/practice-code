package org.test.design.strategy;

/**
 * @author 肖龙威
 * @date 2022/06/23 11:09
 */
public class Test {

    public static void main(String[] args) {
        Strategy five = new FiveStrategy();
        Strategy ten = new TenStrategy();

        Lottery lottery = new Lottery(five);
        lottery.lottery();
        lottery.setStrategy(ten);
        lottery.lottery();
    }
}
