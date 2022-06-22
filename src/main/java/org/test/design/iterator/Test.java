package org.test.design.iterator;

/**
 * @author 肖龙威
 * @date 2022/06/22 10:57
 */
public class Test {

    public static void main(String[] args) {
        Company company = new ConcreteCompany(5);
        company.add("销售");
        company.add("开发");
        company.add("管理");
        company.add("财务");
        company.add("人力");

        DeptIterator iterator = company.getIterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}
