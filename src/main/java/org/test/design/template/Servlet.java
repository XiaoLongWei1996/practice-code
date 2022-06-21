package org.test.design.template;

/**
 * 模板设计模式:父类封装了不变的地方,子类扩展可变的地方
 * HttpServlet就是典型的模板模式
 * @author 肖龙威
 * @date 2021/05/17 9:31
 */
public abstract class Servlet {

    //模板方法
    public void service() {
        switch (hook()) {
            case "post":
                doPost();
                break;
            case "get":
                doGet();
                break;
            default:
                break;
        }
    }

    //钩子方法
    String hook() {
        return "";
    }

    //抽象方法
    abstract void doPost();

    //抽象方法
    abstract void doGet();


}
