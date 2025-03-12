package com.xlw.test.template_demo.config.annotation;

import java.lang.annotation.*;

/**
 * @author xlw
 * @description: Excel标题别名
 * @title: ExcelAlias
 * @package com.piaoshui.ncp.annotation
 * @date 2025/3/12 11:20
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelAlias {

    String value() default "";
}
