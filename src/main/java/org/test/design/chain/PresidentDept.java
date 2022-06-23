package org.test.design.chain;

/**
 * @author 肖龙威
 * @date 2021/05/18 11:27
 */
public class PresidentDept extends Dept {

    public PresidentDept(Dept next, String name) {
        super(next, name);
    }

    public PresidentDept() {
    }

    @Override
    public void handle(Integer count) {
        if (count >= 1000) {
            System.out.println(getName() + "处理");  //当前对象处理
        } else {
            getNext().handle(count);   //传递下一个对象处理
        }
    }
}
