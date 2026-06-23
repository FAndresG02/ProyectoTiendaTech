package com.ec.tecnologia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Imágenes subidas por usuarios — fuera del JAR
        registry.addResourceHandler("/images/**")
                .addResourceLocations(
                        "file:" + System.getProperty("user.dir") + "/uploads/images/",
                        "classpath:/static/images/"  // ← fallback a la imagen por defecto
                );
    }
}