package org.test.design.memento;

/**
 * 备忘录设计模式:用一个类保存另一个类的状态,并且可以恢复
 * @author 肖龙威
 * @date 2021/05/20 16:03
 */
public class Memento {

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Memento(Integer count) {
        this.count = count;
    }
}
