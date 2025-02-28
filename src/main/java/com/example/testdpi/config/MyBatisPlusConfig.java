package com.example.testdpi.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {
    //添加配置项，否则分页查询时返回的ipage中total和pages一直为0
    @Bean
    public MybatisPlusInterceptor paginationInterceptor(){
        MybatisPlusInterceptor interceptor=new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor=new PaginationInnerInterceptor(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
