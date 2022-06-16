package org.test.design.memento;

import java.util.Stack;

/**
 * @author 肖龙威
 * @date 2021/05/20 16:06
 */
public class MementoManager {

    private Stack<Memento> mementos = new Stack<>();

    public void store(Memento memento){
        mementos.push(memento);
    }

    public Memento restore(){
        return mementos.pop();
    }

}
