package com.halo.core_bridge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 예: GET /files/sample.pdf → /portfolios/sample.pdf
        registry.addResourceHandler("/api/pdf/**")
                .addResourceLocations("file:/portfolios/");// 선택: 캐시 1시간
    }

}