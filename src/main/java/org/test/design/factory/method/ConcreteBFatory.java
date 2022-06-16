package org.test.design.factory.method;

/**
 * @author 肖龙威
 * @date 2021/04/19 15:28
 */
public class ConcreteBFatory implements AbstractFatory {

    @Override
    public BProduct newProduct() {
        return new BProduct();
    }
}
