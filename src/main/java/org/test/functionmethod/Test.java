package org.test.functionmethod;

import org.test.design.stream.Student;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Title: Test
 * @Author xlw
 * @Package org.test.functionmethod
 * @Date 2023/6/16 23:54
 * @description: 测试
 */
public class Test {


    public static <T, K> void tt(Function<T, K> function, T t) {
        K k = function.apply(t);
        System.out.println(k);
    }

    public static <T> void tt1(Consumer<T> consumer, T t) {
       consumer.accept(t);
    }

    public static <T> void tt2(Supplier<T> supplier) {
        T t = supplier.get();
        System.out.println(t);
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("小米");
        //Test.tt(Student::getName, student);
        //Test.tt1(Student::getName, student);
        Test.tt2(student::getName);
    }
}
