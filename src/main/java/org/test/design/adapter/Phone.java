package org.test.design.adapter;

/**
 * @author 肖龙威
 * @date 2021/04/29 14:17
 */
public class Phone {

    //充电
    public void charge(Integer in){
        if (in.equals(60)) {
            System.out.println("充电完成");
        }else {
            System.out.println("充电失败");
        }
    }
}
