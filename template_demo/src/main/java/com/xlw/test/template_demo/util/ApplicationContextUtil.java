package com.xlw.test.template_demo.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @Title: ApplicationContextUtil
 * @Author xlw
 * @Package com.igeshui.piaoshui.util
 * @Date 2024/10/23 18:12
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<T> clazz) {
        try {
            T bean = applicationContext.getBean(clazz);
            return bean;
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
