package com.example.testdpi.config;

import com.example.testdpi.Util.JwtInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//此配置类是配置JWT认证的
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private JwtInterceptor jwtInterceptor;
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
//        指定controller统一的接口前缀—"/api"、"/bobo"等等，自己任意配置
        configurer.addPathPrefix("/api",clazz ->clazz.isAnnotationPresent(RestController.class));
    }

    //    加入自定义拦截器JwtInterceptor，设置拦截规制
    @Override
    public void addInterceptors(InterceptorRegistry registry){
//        addPathPatterns("/api/**")加入规则
//        excludePathPatterns("/api/login")排除掉登录和注册这个接口，就不用拦截这两个接口
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/export")
                .excludePathPatterns("/api/user/upload")
                .excludePathPatterns("/api/form/export/**")
                .excludePathPatterns("/api/form/upload/**")
                .excludePathPatterns("/api/user/uploadtest");
    }
}
