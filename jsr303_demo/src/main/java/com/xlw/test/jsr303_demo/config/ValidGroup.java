package com.xlw.test.jsr303_demo.config;

import javax.validation.groups.Default;

/**
 * @description: jsr303分组
 * @Title: ValidGroup
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.config
 * @Date 2024/2/3 17:35
 */
public interface ValidGroup {

    /**
     * 插入组,继承默认组
     */
    interface Insert extends Default {}

    /**
     * 更新组,继承默认组
     */
    interface Update extends Default {}
}
