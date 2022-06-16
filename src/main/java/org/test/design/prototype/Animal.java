package org.test.design.prototype;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 深拷贝:会把基本变量和引用变量都复制一份给新对象,使用序列化和反序列化的方式
 * @author 肖龙威
 * @date 2021/04/20 9:32
 */
@Data
public class Animal implements Serializable {

    private static final long serialVersionUID = 3543757733585080334L;

    private String name;

    private int age;

    private Date birth;


}
