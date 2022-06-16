package org.test.design.factory.abstraction;

/**
 * 抽象工厂设计模式
 * @author 肖龙威
 * @date 2021/04/20 9:01
 */
public class ConcreteFactory implements AbstractFactory {

    @Override
    public CProduct productC() {
        return new CProduct();
    }

    @Override
    public DProduct productD() {
        return new DProduct();
    }
}
