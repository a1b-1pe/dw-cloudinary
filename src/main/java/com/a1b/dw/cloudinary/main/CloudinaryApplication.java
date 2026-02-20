package com.a1b.dw.cloudinary.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.a1b.dw.cloudinary"})
@SpringBootApplication
public class CloudinaryApplication
{
	public static void main(String[] args) {
		SpringApplication.run(CloudinaryApplication.class, args);
	}
	
}