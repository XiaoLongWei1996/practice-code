package org.test.design.template;

/**
 * @author 肖龙威
 * @date 2021/05/17 16:02
 */
public class HttpGetServlet extends Servlet{

    @Override
    public String hook() {
        return "get";
    }

    @Override
    public void doPost() {
        throw new RuntimeException("该类不支持post方法");
    }

    @Override
    public void doGet() {
        System.out.println("get执行");
    }
}
