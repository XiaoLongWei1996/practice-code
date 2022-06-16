package org.test.design.observer;

/**
 * @author 肖龙威
 * @date 2021/05/18 16:42
 */
public class CCTVObserver implements Observer {

    @Override
    public void notice(String update) {
        System.out.println("CCTV预报:" + update);
    }
}
