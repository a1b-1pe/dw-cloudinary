package com.a1b.dw.cloudinary.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.a1b.dw.cloudinary")
public class CloudinaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudinaryApplication.class, args);
    }
}
