package org.test.design.bulider;

/**
 * 具体的构建类,实现具体的构建方法,不同的构建类,可以构造不同功能的对象
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
