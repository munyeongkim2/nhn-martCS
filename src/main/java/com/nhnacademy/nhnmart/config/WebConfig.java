package com.nhnacademy.nhnmart.config;

import com.nhnacademy.nhnmart.interceptor.LoginInterceptor;
import com.nhnacademy.nhnmart.interceptor.UserCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 'src/main/resources/upload/' 경로에 있는 파일들을 '/uploads/**' URL 패턴으로 서빙
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {//스프링이 주입해줌
        registry.addInterceptor(new LocaleChangeInterceptor());
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/cs/**");
        registry.addInterceptor(new UserCheckInterceptor()).addPathPatterns("/cs/admin/**");
    }

}
