package org.test.design.factory.method;

/**
 * @author 肖龙威
 * @date 2021/04/19 15:27
 */
public class ConcreteAFatory implements AbstractFatory {

    @Override
    public AProduct newProduct() {
        return new AProduct();
    }
}
