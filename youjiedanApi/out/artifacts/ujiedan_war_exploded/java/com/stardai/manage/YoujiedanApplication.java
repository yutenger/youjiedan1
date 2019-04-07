package com.stardai.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages="com.stardai.manage")
@MapperScan("com.stardai.manage.mapper")
@ServletComponentScan
public class YoujiedanApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(YoujiedanApplication.class, args);
    }


    /**
     *新增此方法
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(YoujiedanApplication.class);
    }


}
