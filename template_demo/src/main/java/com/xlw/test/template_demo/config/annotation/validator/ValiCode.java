package com.xlw.test.template_demo.config.annotation.validator;

import com.piaoshui.ncp.config.valid.ValiCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ValiCodeValidator.class}) //
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValiCode {
    String message() default "码值不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum> vals();//
}