package org.test.design.bulider;

/**
 * @author 肖龙威
 * @date 2022/06/17 16:26
 */
public class Test {

    public static void main(String[] args) {
        //创建构造器
        AbstractBulider bulider = new ConcreteBulider();
        //执行构造流程的指挥者对象
        Director director = new Director(bulider);
        //得到构造好的对象
        Product product = director.construct();
        System.out.println(product);
    }
}
