package org.test.design.chain;

/**
 * @author 肖龙威
 * @date 2021/05/18 11:22
 */
public class ManagerDept extends Dept {

    public ManagerDept(Dept next, String name) {
        super(next, name);
    }

    public ManagerDept() {
    }

    @Override
    public void handle(Integer count) {
        if (500 <= count && count < 1000) {
            System.out.println(getName() + "处理"); //当前对象处理
        } else {
            getNext().handle(count); //传递下一个对象去处理
        }
    }
}
