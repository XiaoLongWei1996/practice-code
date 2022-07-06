package org.test.design.factory.abstraction;

/**
 * 抽象工厂:创建的是一个系列类的对象
 * @author 肖龙威
 * @date 2021/04/19 15:34
 */
public interface AbstractFactory {

    CProduct productC();

    DProduct productD();
}
