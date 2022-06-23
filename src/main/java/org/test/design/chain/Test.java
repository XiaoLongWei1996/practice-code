package org.test.design.chain;

/**
 * @author 肖龙威
 * @date 2022/06/23 14:02
 */
public class Test {

    public static void main(String[] args) {
        //创建处理数据对象
        Dept president = new PresidentDept();
        Dept manager = new ManagerDept();
        Dept personnel = new PersonnelDept();

        //形成责任链
        president.setName("宋江");
        president.setNext(manager);

        manager.setName("吴用");
        manager.setNext(personnel);

        personnel.setName("李逵");
        personnel.setNext(president);

        //处理数据
        manager.handle(100);
        personnel.handle(5000);
    }
}
