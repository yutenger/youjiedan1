package com.stardai.manage.config;

import com.stardai.manage.bean.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @program: ujiedan
 * @Date: 2018/7/25 17:36
 * @Author: Tina
 * @Description:拦截器的配置
 */

@Configuration
public class WebConfig implements  WebMvcConfigurer {
    @Resource
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }
}
