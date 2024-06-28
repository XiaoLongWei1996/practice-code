package org.test.study;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 盲盒
 * @Title: BlindBox
 * @Author xlw
 * @Package org.test.study
 * @Date 2024/6/24 16:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlindBox {

    /**
     * id
     */
    private int id;

    /**
     * 商品名字
     */
    private String name;

    /**
     * 获得权重
     */
    private int weight;
}
