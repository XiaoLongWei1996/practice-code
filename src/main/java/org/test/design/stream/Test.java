package org.test.design.stream;

import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/06/17 16:49
 */
public class Test {

    public static void main(String[] args) {
        Student student = Student.builder().age(1).name("小明").birth(new Date()).build();
        System.out.println(student);
    }
}
