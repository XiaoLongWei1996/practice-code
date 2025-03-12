package com.xlw.test.template_demo.config.annotation.validator;


import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.util.StrUtil;
import com.piaoshui.ncp.annotation.Cascade;
import com.piaoshui.ncp.annotation.Related;
import com.piaoshui.ncp.annotation.Relatively;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @description: 关联校验器
 * @Title: RelatedContraintValidator
 * @Author xlw
 * @Package com.xlw.test.jsr303_demo.annotation.validator
 * @Date 2024/11/16 10:47
 */
public class RelatedConstraintValidator implements ConstraintValidator<Related, Object> {

    /**
     * 级联关系
     */
    private Cascade[] cascades;

    /**
     * 相对关系
     */
    private Relatively[] relativelys;

    /**
     * 任意字段不为字段
     */
    private String[] anyOneNotNullFields;

    /**
     * 消息
     */
    private String message;

    @Override
    public void initialize(Related constraintAnnotation) {
        cascades = constraintAnnotation.cascades();
        relativelys = constraintAnnotation.relativelys();
        anyOneNotNullFields = constraintAnnotation.anyOneNotNullFields();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) {
            return false;
        }

        DynaBean bean = DynaBean.create(o);
        if (cascades.length > 0) {
            //校验级联关系
            for (Cascade cascade : cascades) {
                Object v1 = bean.get(cascade.field1());
                Object v2 = bean.get(cascade.field2());
                //两个字段不同时为空或者不为空报错
                if ((v1 == null && v2 != null) || (v1 != null && v2 == null)) {
                    if (StrUtil.isBlank(message)) {
                        message = "字段" + cascade.field1() + "与字段" + cascade.field2() + "必须同时为空或同时不为空";
                    }
                    addMessage(constraintValidatorContext, message);
                    return false;
                }
            }
        }

        if (relativelys.length > 0) {
            for (Relatively relatively : relativelys) {
                Object v1 = bean.get(relatively.field1());
                Object v2 = bean.get(relatively.field2());
                //两字段同时为空或者不为空时报错
                if ((v1 == null && v2 == null) || (v1 != null && v2 != null)) {
                    if (StrUtil.isBlank(message)) {
                        message = "字段" + relatively.field1() + "与字段" + relatively.field2() + "不能同时为空或者不为空";
                    }
                    addMessage(constraintValidatorContext, message);
                    return false;
                }
            }
        }

        if (anyOneNotNullFields.length > 0) {
            for (String field : anyOneNotNullFields) {
                Object v = bean.get(field);
                if (v != null) {
                    return true;
                }
            }
            if (StrUtil.isBlank(message)) {
                message = "字段" + StrUtil.join(",", anyOneNotNullFields) + "至少有一个不为空";
            }
            addMessage(constraintValidatorContext, message);
            return false;
        }

        return true;
    }

    /**
     * 添加消息
     *
     * @param context 上下文
     * @param message 消息
     */
    private void addMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
