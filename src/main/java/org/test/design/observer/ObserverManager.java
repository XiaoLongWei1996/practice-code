package org.test.design.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者管理类,可以把事件的执行分发给多个观察者
 * @author 肖龙威
 * @date 2021/05/18 16:02
 */
public class ObserverManager {

    private List<Observer> observers = new ArrayList<>();

    public void register(Observer observer){
        observers.add(observer);
    }

    public void cancel(Observer observer){
        observers.remove(observer);
    }

    public void notice(String update){
        observers.forEach((x) -> {
            x.notice(update);
        });
    }
}
