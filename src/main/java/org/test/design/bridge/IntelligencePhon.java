package org.test.design.bridge;

import lombok.AllArgsConstructor;

/**
 * @author 肖龙威
 * @date 2021/04/29 15:22
 */
@AllArgsConstructor
public class IntelligencePhon implements Phone {

    private Brand brand;

    @Override
    public void call() {
        brand.call();
        System.out.println("智能手机");
    }
}
