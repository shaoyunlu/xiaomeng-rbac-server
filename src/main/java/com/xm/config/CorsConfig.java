package com.xm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 对所有路径应用CORS设置
                registry.addMapping("/**")
                        // 允许从这些来源的跨域请求
                        .allowedOrigins("http://localhost:3000" ,"http://114.116.50.8:3000")
                        // 允许发送这些请求方法
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        // 允许这些头部
                        .allowedHeaders("*")
                        // 允许携带认证信息（如Cookies）
                        .allowCredentials(true)
                        // 预检请求的结果缓存时间（秒）
                        .maxAge(3600);
            }
        };
    }
}
