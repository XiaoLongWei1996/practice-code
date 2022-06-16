package org.test.design.bulider;

/**
 * 产品
 * @author 肖龙威
 * @date 2021/04/20 9:06
 */
public class Product {

    private String describe;

    private String name;

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println(name + ":" + describe);
    }
}
