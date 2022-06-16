package org.test.design.bulider;

/**
 * 指挥者，调用bulider完成复杂对象的构建
 * @author 肖龙威
 * @date 2021/04/20 9:15
 */
public class Director {

    private AbstractBulider bulider;

    public Director(AbstractBulider bulider) {
        this.bulider = bulider;
    }

    public Product construct(){
        bulider.setName();
        bulider.setDescribe();
        return bulider.getResult();
    }
}
