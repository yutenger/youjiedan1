package com.stardai.manage.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 系统bean帮助类
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    @Autowired
    private static ApplicationContext context;

    @Override
    @SuppressWarnings("static-access")
    public void setApplicationContext(ApplicationContext contex)
            throws BeansException {
        // TODO Auto-generated method stub
        this.context = contex;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
    public static <T>T getBean(Class<T> clz) {
        return context.getBean(clz);
    }
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

}
