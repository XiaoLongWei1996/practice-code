package org.test.design.observer;

/**
 * 相当于事件,当这个类的某方法执行时,就会通知观察者执行
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
        observerManager.notice(weather);  //观察者管理器通知观察者执行
    }
}
