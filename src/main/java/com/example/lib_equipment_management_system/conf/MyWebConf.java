package com.example.lib_equipment_management_system.conf;

import com.example.lib_equipment_management_system.intercept.UserIntercept;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConf implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedMethods("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UserIntercept intercept = getUserIntercept();
        registry.addInterceptor(intercept)
                .addPathPatterns("/data/**")
                .excludePathPatterns("/user");

    }

    @Bean
    public UserIntercept getUserIntercept() {
        return new UserIntercept();
    }


}
