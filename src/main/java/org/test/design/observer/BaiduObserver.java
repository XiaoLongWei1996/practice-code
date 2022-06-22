package org.test.design.observer;

/**
 * 具体的观察者
 * @author 肖龙威
 * @date 2021/05/18 16:41
 */
public class BaiduObserver implements Observer {

    @Override
    public void notice(String update) {
        System.out.println("百度预报:" + update);
    }
}
