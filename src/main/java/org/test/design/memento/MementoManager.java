package org.test.design.memento;

import java.util.Stack;

/**
 * 备份管理器,用来保存所有的备份对象
 * @author 肖龙威
 * @date 2021/05/20 16:06
 */
public class MementoManager {

    private Stack<Memento> mementos = new Stack<>(); //栈保存备份对象,先进后出

    public void store(Memento memento){
        mementos.push(memento);
    }  //添加备份对象

    public Memento restore(){  //弹出备份对象
        return mementos.pop();
    }

}
