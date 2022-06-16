package org.test.reflex;

/**
 * @author 肖龙威
 * @date 2022/02/24 14:27
 */
public enum MyClass {

    MY_CLASS01(1),

    MY_CLASS02(2);

    public int code;

    MyClass(int code){
        this.code = code;
    }

    public String getCode(){
        return "1";
    }
}
