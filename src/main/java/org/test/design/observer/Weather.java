package org.test.design.observer;

/**
 * 具体的事件
 * @author 肖龙威
 * @date 2021/05/18 16:35
 */
public class Weather extends Subject {

    public Weather(ObserverManager observerManager) {
        super(observerManager);
    }

    @Override
    public void doing(String weather) {
        System.out.println("天气:" + weather);
    }
}
