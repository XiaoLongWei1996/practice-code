package com.xlw.test.template_demo.config.annotation;

import com.piaoshui.ncp.config.valid.DateFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {DateFormatValidator.class}) //
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValiDateFormat {
    String message() default "日期格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String format() default "yyyy-MM-dd HH:mm:ss";//
}