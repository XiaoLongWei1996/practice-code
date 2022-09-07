package org.test.design.bulider.reflex;

/**
 * @author 肖龙威
 * @date 2022/01/17 16:16
 */
public class Student extends Parent implements Parent01 {

    public final int i;

    static {
        System.out.println("执行");
    }

    public Student(int i){
        this.i = i;
    }
}

