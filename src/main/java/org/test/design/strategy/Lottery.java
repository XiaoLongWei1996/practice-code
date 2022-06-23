package org.test.design.strategy;

/**
 * 抽奖
 *
 * @author 肖龙威
 * @date 2021/05/17 16:58
 */
public class Lottery {

    private Strategy strategy;

    //构造器方式依赖策略对象
    public Lottery(Strategy strategy) {
        this.strategy = strategy;
    }

    //set方式依赖策略对象
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    //需要用到不同策略的方法
    public void lottery() {
        for (int i = 0; i <= 50; i++) {
            Integer n = this.strategy.strategy(); //不同策略的不同方法
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
