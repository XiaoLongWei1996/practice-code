package org.test.design.prototype;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 原型设计模式:会复制一份和原型对象属性一样的对象
 * 浅拷贝:会把基本变量复制一份给新对象，引用对象把引用地址给新的对象,实现Cloneable接口
 * @author 肖龙威
 * @date 2021/04/20 9:25
 */
@Data
public class People implements Cloneable, Serializable {

    private int age;

    private String name;

    private Date birth;

    private Animal animal;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
