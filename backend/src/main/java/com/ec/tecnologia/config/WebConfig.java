package com.ec.tecnologia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/images/**") // cambia el patrón aquí
                .addResourceLocations(
                        "file:" + System.getProperty("user.dir") + "/uploads/images/",
                        "classpath:/static/images/"
                );
    }
}