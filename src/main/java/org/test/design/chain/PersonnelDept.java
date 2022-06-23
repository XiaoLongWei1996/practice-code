package org.test.design.chain;

/**
 * @author 肖龙威
 * @date 2021/05/18 11:12
 */
public class PersonnelDept extends Dept{

    public PersonnelDept(Dept next, String name) {
        super(next, name);
    }

    public PersonnelDept() {

    }

    @Override
    public void handle(Integer count) {
        if (count < 500) {
            System.out.println(getName() + "处理"); //当前对象处理
        } else {
            getNext().handle(count); //传递下一个对象去处理
        }
    }
}
