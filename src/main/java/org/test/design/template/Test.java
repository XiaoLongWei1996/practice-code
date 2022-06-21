package org.test.design.template;

/**
 * @author 肖龙威
 * @date 2022/06/21 15:45
 */
public class Test {

    public static void main(String[] args) {
        HttpPostServlet postServlet = new HttpPostServlet();
        postServlet.service();

        HttpGetServlet getServlet = new HttpGetServlet();
        getServlet.service();
    }
}
