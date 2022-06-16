package org.test.design.template;

/**
 * @author 肖龙威
 * @date 2021/05/17 15:57
 */
public class HttpPostServlet extends Servlet {

    @Override
    public String hook() {
        return "post";
    }

    @Override
    public void doPost() {
        System.out.println("post执行");
    }

    @Override
    public void doGet() {
        System.out.println("get执行");
    }
}
