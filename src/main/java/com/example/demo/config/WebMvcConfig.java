package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")	// 모든 경로에 대해
			.allowedOrigins("http://localhost:3000") // Origin이 http://localhost:3000에 대해
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 다음과 같은 메소드 허용
			.allowedHeaders("*")
			.allowCredentials(true).maxAge(MAX_AGE_SECS);		
	}
} 