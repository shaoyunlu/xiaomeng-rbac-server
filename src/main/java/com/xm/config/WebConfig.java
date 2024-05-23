package com.xm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xm.cache.PermissionCache;
import com.xm.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private PermissionCache permissionCache;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(permissionCache))
                .addPathPatterns("/**")
                .excludePathPatterns("/captcha" ,"/login","/logout","/session/user");
    }
}
