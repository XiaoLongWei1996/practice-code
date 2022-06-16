package org.test.design.strategy;

import java.util.Random;

/**
 * @author 肖龙威
 * @date 2021/05/17 16:53
 */
public class TenStrategy implements Strategy {

    @Override
    public Integer strategy() {
        Random random = new Random();
        int i = random.nextInt(10);
        i = i + 1;
        return i;
    }
}
