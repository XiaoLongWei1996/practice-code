package org.test.design.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体中介对象,管理对象
 * @author 肖龙威
 * @date 2021/05/20 13:57
 */
public class EstateMedium implements Mediator {

    private List<Customer> customerList;

    public EstateMedium() {
        this.customerList = new ArrayList<>();
    }


    @Override
    public void registar(Customer customer) {
        if (!customerList.contains(customer)){
            customer.setMediator(this);
            customerList.add(customer);
        }
    }

    @Override
    public void relay(Customer customer, String text) {
        for (Customer c : customerList) {
            if (!c.equals(customer)){
                c.receive(customer, text);
            }
        }
    }
}
