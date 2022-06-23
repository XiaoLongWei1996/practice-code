package org.test.design.strategy;

import java.util.Random;

/**
 * 具体策略
 * @author 肖龙威
 * @date 2021/05/17 16:41
 */
public class FiveStrategy implements Strategy {

    @Override
    public Integer strategy() {
        Random random = new Random();
        int i = random.nextInt(5);
        i = i + 1;
        return i;
    }
}
