package org.test.design.strategy;

/**
 * 抽奖
 *
 * @author 肖龙威
 * @date 2021/05/17 16:58
 */
public class Lottery {

    private Strategy strategy;

    public Lottery(Strategy strategy) {
        this.strategy = strategy;
    }

    public void lottery() {
        for (int i = 0; i <= 50; i++) {
            Integer n = this.strategy.strategy();
            if (n.equals(1)) {
                System.out.println("一等奖");
            }

            if (n.equals(2)) {
                System.out.println("二等奖");
            }

            if (n.equals(3)) {
                System.out.println("三等奖");
            }
        }
    }
}
