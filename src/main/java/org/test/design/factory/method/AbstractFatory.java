package org.test.design.factory.method;

/**
 * 工厂方法设计模式:不同类型的类有不同的工厂
 * 工厂接口
 * @author 肖龙威
 * @date 2021/04/19 15:09
 */
public interface AbstractFatory {

    /**
     * 生产产品的工厂接口
     * @return 产品对象
     */
    Product newProduct();
}
