package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("test")
@Log4j2
public class TestController {
	
	@GetMapping("/testGetMapping")
	public String testController() {
		
		return "Hello World!";
	}
	
	@GetMapping("/{id}")
	public String testControllerWithPathVariable(@PathVariable(required = false) int id) {
		
		log.info("\t+ testControllerWithPathVariable({}) controller invoked", id);
		
		return "Hello Wrold! ID " + id;
	}
	
	@GetMapping("/testRequestParam")
	public String testControllerWithRequsetParam(@RequestParam("id") int id2) {
		
		log.info("\t+ testControllerWithRequsetParam({}) controller invoked", id2);
		
		return "Hello World! ID " + id2;
		
	}
	
	@GetMapping("/testRequestBody")
	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO dto) {
		
		log.info("\t+ testControllerRequestBody({}) controller invoked.", dto);
		
		return "Hello World! ID " + dto.getId() + ", Message " + dto.getMessage();
	}
	
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testControllerResponseBody(){
		
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseDTO");
		list.add("Hello Seoul!");
		ResponseDTO<String> res = ResponseDTO
										.<String>builder().data(list).build(); // 빌더패턴 사용
		
		return res;
	}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity(){
		
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<String> res = ResponseDTO.<String>builder().data(list).build();
		
		// http status를 400으로 설정하고 body에 res를 담기
		return ResponseEntity.badRequest().body(res);
	}
}