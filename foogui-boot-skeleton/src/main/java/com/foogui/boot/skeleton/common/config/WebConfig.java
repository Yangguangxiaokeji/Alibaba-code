package com.foogui.boot.skeleton.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    // @Resource
    // private RateLimitInterceptor rateLimitInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry 注册表
     */
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/**");
    // }


    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     registry.addResourceHandler("/uploads/**")
    //             .addResourceLocations("file:/Users/xlang/Code/train/alibaba-faw-source/uploads/");
    // }
}
