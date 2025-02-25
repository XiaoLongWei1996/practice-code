package com.xlw.test.template_demo.util;


import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 上下文帮助类
 * @Title: SpringContextHelper
 * @Author xlw
 * @Package com.xlw.spring_design_demo.util
 * @Date 2025/1/17 9:45
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    /**
     * -- GETTER --
     *  获取应用程序上下文
     *
     * @return {@link ApplicationContext }
     */
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取 Bean
     *
     * @param clazz 克拉兹
     * @return {@link T }
     */
    public synchronized static <T> T getBean(Class<T> clazz) {
        try {
            T bean = applicationContext.getBean(clazz);
            return bean;
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 获取 Bean
     *
     * @param name 名字
     * @return {@link Object }
     */
    public synchronized static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 获取 bean 类型
     *
     * @param clazz 克拉兹
     * @return {@link Map }<{@link String }, {@link T }>
     */
    public synchronized static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        try {
            return applicationContext.getBeansOfType(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 发布事件
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }
}
