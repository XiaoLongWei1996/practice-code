package org.test.design.prototype.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @description: 学生
 * @Title: Student
 * @Author xlw
 * @Package org.test.design.prototype.bean
 * @Date 2023/7/27 17:33
 */
@Data
@AllArgsConstructor
public class Student {

    private String name;

    private Integer age;



    public void a(Supplier<String> supplier) {
        System.out.println(supplier.get());
    }

    public void b(Consumer<Student> consumer) {
        consumer.accept(this);
    }

    public String c(Function<Student, String> function) {
        return function.apply(this);
    }
}
