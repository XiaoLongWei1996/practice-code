package org.test.design.memento;

/**
 * @author 肖龙威
 * @date 2022/06/22 17:09
 */
public class Test {

    public static void main(String[] args) {
        Originator originator = new Originator();
        originator.setCount(2);
        originator.bak();   //备份数据2
        originator.setCount(5);
        System.out.println(originator.getCount());
        originator.recover();  //恢复数据2
        System.out.println(originator.getCount());
    }
}
