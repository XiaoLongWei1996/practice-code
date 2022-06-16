package org.test.design.bridge;

import lombok.Data;

/**
 * @author 肖龙威
 * @date 2021/04/29 14:49
 */
@Data
public abstract class Brand{

    private String brand;

    protected Brand(String brand){
        this.brand = brand;
    }

    public void call(){
        System.out.println(brand);
    }
}
