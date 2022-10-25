package com.springcloud.test.home.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/10/25 15:29
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Dept {

    private Integer id;

    private String name;

    private String code;

    private Integer type;

    private Integer isDelete;

    private Date createDt;

    private Date updateDt;
}
