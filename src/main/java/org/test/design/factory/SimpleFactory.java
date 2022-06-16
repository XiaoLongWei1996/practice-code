package org.test.design.factory;

/**
 * 简单工厂设计模式
 * @author 肖龙威
 * @date 2021/04/19 13:49
 */
public class SimpleFactory {

    public static String product(Integer type){
        switch (type) {
            case 1 :
                return "A";
            case 2 :
                return "B";
            case 3 :
                return "C";
            default:
                return "D";
        }
    }

}
