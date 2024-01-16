package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service // 서비스 레이어임을 알려주는 어노테이션
public class TodoService {
	
	public String testService() {
		
		return "Test Service";
	}
	
} // end class