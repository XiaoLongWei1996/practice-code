package org.test.design.composite;

/**
 * @author 肖龙威
 * @date 2021/05/17 9:00
 */
public class Leaf implements Component {

    private String name;

    public Leaf(String name) {
        this.name = name;
    }

    @Override
    public void opration() {
        System.out.println(this.name + "执行操作");
    }
}
