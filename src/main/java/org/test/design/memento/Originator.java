package org.test.design.memento;

/**
 * @author 肖龙威
 * @date 2021/05/20 16:04
 */
public class Originator {

    private Integer count;

    private MementoManager mementoManager = new MementoManager();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void bak(){
        mementoManager.store(new Memento(count));
    }

    public void recover(){
        this.count = mementoManager.restore().getCount();
    }
}
