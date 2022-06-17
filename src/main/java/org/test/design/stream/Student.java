package org.test.design.stream;

import lombok.Data;

import java.util.Date;

/**
 * 流程设计模式,创建对象可以用流的模式创建对象
 * @author 肖龙威
 * @date 2022/06/17 16:38
 */
@Data
public class Student {

    private String name;

    private int age;

    private Date birth;

    public Student() {

    }

    public Student(String name, int age, Date birth) {
        this.name = name;
        this.age = age;
        this.birth = birth;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;

        private int age;

        private Date birth;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder birth(Date birth) {
            this.birth = birth;
            return this;
        }

        public Student build(){
            return new Student(name, age, birth);
        }
    }
}
