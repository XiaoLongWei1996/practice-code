package org.test.design.bulider;

/**
 * @author 肖龙威
 * @date 2021/04/20 9:13
 */
public class ConcreteBulider extends AbstractBulider {
    @Override
    void setName() {
        product.setName("八宝粥");
    }

    @Override
    void setDescribe() {
        product.setDescribe("桂圆八宝粥");
    }
}
