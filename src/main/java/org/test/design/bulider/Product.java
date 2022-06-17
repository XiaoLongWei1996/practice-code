package org.test.design.bulider;

import lombok.Data;

/**
 * 产品,需要被创建的对象
 * @author 肖龙威
 * @date 2021/04/20 9:06
 */
@Data
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
