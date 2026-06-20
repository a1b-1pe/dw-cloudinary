package com.a1b.dw.cloudinary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v1/cloudinary/**")
            .allowedOrigins(
                "https://dayworks.co",
                "https://workday24-fd282.web.app",
                "http://localhost:8080",
                "http://localhost:5173",
                "https://localhost:5173"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");
    }
}
