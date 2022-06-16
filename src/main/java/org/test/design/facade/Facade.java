package org.test.design.facade;

import lombok.AllArgsConstructor;

/**
 * 外观设计模式:就是设计一个界面类统一操作一系列的子系统
 * @author 肖龙威
 * @date 2021/04/29 16:28
 */
@AllArgsConstructor
public class Facade {

    ASystem aSystem;

    BSystem bSystem;

    CSystem cSystem;

    public void start(){
        aSystem.startA();
        bSystem.startB();
        cSystem.startC();
    }

    public void run(){
        aSystem.runA();
        bSystem.runB();
        cSystem.startC();
    }

    public void close(){

    }
}
