package com.xlw.test.template_demo.cons;

import javax.validation.groups.Default;

/**
 * @description: 校验分组
 * @Title: ValidGroup
 * @Author xlw
 * @Package com.security.common.constant
 * @Date 2024/4/2 17:50
 */
public interface ValidGroup {


    /**
     * 插入组,继承默认组
     */
    interface Insert extends Default {
    }

    /**
     * 更新组,继承默认组
     */
    interface Update extends Default {
    }


}
