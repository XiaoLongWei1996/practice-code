package org.test.design.flyweight;

/**
 * @author 肖龙威
 * @date 2021/05/08 16:36
 */
public class Bicycle implements Transportation {

    private String name;

    public Bicycle(String name){
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(this.name + "自行车运动");
    }
}
