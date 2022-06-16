package org.test.design.bulider;

/**
 * 建造者模式,用于建造复杂的对象
 * @author 肖龙威
 * @date 2021/04/20 9:09
 */
public abstract class AbstractBulider {

    //产品对象
    protected Product product = new Product();

    //构建产品对象，根据具体的实现类
    abstract void setName();

    abstract void setDescribe();

    //返回产品对象
    public final Product getResult() {
        return product;
    }

}
