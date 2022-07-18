package com.shan.config;

import com.shan.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //登录拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;

    //跨域配置，解决前端访问后端的跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
//                .allowedOrigins("https://blog.yilele.site");
//        registry.addMapping("/**").allowedOrigins("https://blog.yilele.site");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        //一般通用的拦截（但是博客系统的话，一般不考虑登录注册功能）
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/register");
        //拦截测试接口，后续实际遇到需要拦截的接口时，在配置为真正的拦截接口
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}


