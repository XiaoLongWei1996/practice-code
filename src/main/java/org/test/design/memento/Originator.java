package org.test.design.memento;

/**
 * 具体数据的发起者
 * @author 肖龙威
 * @date 2021/05/20 16:04
 */
public class Originator {

    private Integer count;  //当前数据

    private MementoManager mementoManager = new MementoManager();  //保存备份数据

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    //备份
    public void bak(){
        mementoManager.store(new Memento(count));
    }

    //恢复
    public void recover(){
        this.count = mementoManager.restore().getCount();
    }
}
