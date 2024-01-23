package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.security.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig  {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		log.info("\t+ filterChain({}) invoked.", http);
		
		http.cors()		// WebMvcConfig에서 이미 설정했으므로 기본 cors 설정
			.and()
			.csrf()
			.disable()
			.httpBasic()	// token을 사용하므로 basic 인증 disable
			.disable()
			.sessionManagement()	// session 기반이 아님을 선언
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests() 	// /와 /auth/** 경로는 인증 안해도 됨
			.requestMatchers("/", "/auth/**").permitAll()
			.anyRequest().authenticated();
		
		http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
		
		return http.build();
	}
}