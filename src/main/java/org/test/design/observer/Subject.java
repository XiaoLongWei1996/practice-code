package org.test.design.observer;

/**
 * @author 肖龙威
 * @date 2021/05/18 16:00
 */
public abstract class Subject {

    private ObserverManager observerManager;

    public Subject(ObserverManager observerManager) {
        this.observerManager = observerManager;
    }

    public abstract void doing(String weather);

    public void remind (String weather){
        doing(weather);
        observerManager.notice(weather);
    }
}
