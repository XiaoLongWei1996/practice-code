package org.test.design.adapter;

/**
 * @author 肖龙威
 * @date 2021/04/29 14:22
 */
public class HighVoltage implements Voltage {

    @Override
    public Integer out() {
        return 240;
    }
}
