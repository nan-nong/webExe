package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.TodoService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired // 빈을 찾은 다음 그 빈을 인스턴스 멤버 변수에 연결하라는 어노테이션
	private TodoService service;

//	@GetMapping("/testTodo")
//	public ResponseEntity<?> testTodo(){
//		
//		List<TodoDTO> list = new ArrayList<>();
//		TodoDTO dto = TodoDTO.builder().id("1").title("공부하기").done(false).build();
//		list.add(dto);
//		log.info("\t+" + list);
//		
//		return ResponseEntity.ok().body(list);
//	}
	
	@GetMapping("/test")
	public ResponseEntity<?> testTodo(){
		
		log.info("\t+ testTodo() service invoked.");
		
		String str = service.testService();
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> res = ResponseDTO.<String>builder().data(list).build();
		
		return ResponseEntity.ok().body(res);
	}
	
} // end class