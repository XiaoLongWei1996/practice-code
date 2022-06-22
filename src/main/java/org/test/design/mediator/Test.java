package org.test.design.mediator;

/**
 * @author 肖龙威
 * @date 2022/06/22 15:51
 */
public class Test {

    public static void main(String[] args) {
        Customer buyer = new Buyer("小白", "买家");  //创建买家
        Customer seller = new Seller("大白", "卖家"); //创建卖家
        Mediator mediator = new EstateMedium();  //中介,管理买家和卖家
        mediator.registar(seller);
        mediator.registar(buyer);
        buyer.send("你好");
        seller.send("hello");
    }
}
