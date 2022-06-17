package org.test.design.adapter;

import lombok.AllArgsConstructor;

/**
 * 适配器模式:将目标数据转换成需要的数据(将不兼容的数据转换成兼容的数据)
 * @author 肖龙威
 * @date 2021/04/29 14:27
 */
@AllArgsConstructor
public class VoltageAdapter implements Voltage {

    private Voltage voltage;

    @Override
    public Integer out() {
        Integer out = voltage.out();
        out = out / 4;
        return out;
    }
}
