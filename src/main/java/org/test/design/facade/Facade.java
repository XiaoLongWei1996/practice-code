package org.test.design.facade;

/**
 * 外观设计模式:就是设计一个顶层的类去调用一些列相关的方法,从而避免外部创建多个类,多次调用
 * 从而对外屏蔽子系统的细节
 * @author 肖龙威
 * @date 2021/04/29 16:28
 */
public class Facade {

    private ASystem aSystem;

    private BSystem bSystem;

    private CSystem cSystem;

    public Facade() {
        aSystem = new ASystem();
        bSystem = new BSystem();
        cSystem = new CSystem();
    }

    //封装调用方法,对外屏蔽细节
    public void start(){
        aSystem.startA();
        bSystem.startB();
        cSystem.startC();
    }

    //封装调用方法,对外屏蔽细节
    public void run(){
        aSystem.runA();
        bSystem.runB();
        cSystem.startC();
    }

    //封装调用方法,对外屏蔽细节
    public void close(){
        aSystem.closeA();
        bSystem.closeB();
        cSystem.closeC();
    }
}
