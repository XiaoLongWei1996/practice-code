package org.test.design.strategy;

/**
 * 策略模式:将不同的逻辑代码封装到不同的策略类中,目标类某方法要使用不同功能代码,只需要依赖不同策略对象就行
 * @author 肖龙威
 * @date 2021/05/17 16:30
 */
public interface Strategy {

    Integer strategy();
}
