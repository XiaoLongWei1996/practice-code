package org.test.design.observer;

/**
 * @author 肖龙威
 * @date 2022/06/22 15:31
 */
public class Test {

    public static void main(String[] args) {
        ObserverManager manager = new ObserverManager();
        manager.register(new BaiduObserver());
        manager.register(new CCTVObserver());
        Subject subject = new Weather(manager);
        subject.remind("下雨");
    }
}
