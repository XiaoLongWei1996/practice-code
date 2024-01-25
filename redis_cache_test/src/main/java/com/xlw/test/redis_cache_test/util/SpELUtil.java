package com.xlw.test.redis_cache_test.util;

import cn.hutool.core.lang.Assert;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * @description: SpEL表达式工具类
 * @Title: SpELUtil
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.util
 * @Date 2024/1/10 17:31
 */
public class SpELUtil {


    public static String parseSpEL(String expression, Map<String, Object> parameters) {
        Assert.notBlank(expression, "表达式不能为空");
        Assert.notNull(parameters, "对象不能为空");
        StandardEvaluationContext context = new StandardEvaluationContext();
        //1、设置变量
        context.setVariables(parameters);
        ExpressionParser parser = new SpelExpressionParser();
        //2、表达式中以#varName的形式使用变量
        Expression e = parser.parseExpression(expression);
        //3、在获取表达式对应的值时传入包含对应变量定义的EvaluationContext
        return e.getValue(context).toString();
    }

}
