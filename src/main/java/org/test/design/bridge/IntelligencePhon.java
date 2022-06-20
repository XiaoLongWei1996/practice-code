package org.test.design.bridge;

import lombok.AllArgsConstructor;

/**
 * 桥接器模式,把原本需要多实现,递归继承的类改用聚合的模式;
 * 可以防止创建大量的类,节约资源,提高代码复用,避免了类爆炸
 * @author 肖龙威
 * @date 2021/04/29 15:22
 */
@AllArgsConstructor
public class IntelligencePhon implements Phone {

    private Brand brand;

    @Override
    public void call() {
        brand.call();
        System.out.println("智能手机");
    }
}
