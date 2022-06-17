package org.test.design.adapter;

/**
 * @author 肖龙威
 * @date 2022/06/17 17:31
 */
public class Test {

    public static void main(String[] args) {
        Phone phone = new Phone();
        Voltage highVoltage = new HighVoltage();
        phone.charge(highVoltage.out());
        //使用适配器模式
        Voltage adapter = new VoltageAdapter(highVoltage);
        phone.charge(adapter.out());
    }
}
