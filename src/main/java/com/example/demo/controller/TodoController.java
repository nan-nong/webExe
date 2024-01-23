package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

//@Log4j2
@Slf4j
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
	
	// body에 dto모양의 JSON의 데이터를 전달받으면, ResponseEntity로 응답을 보낸다.
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,
										@RequestBody TodoDTO dto){
		
		try {
			log.info("\t+ createTodo({}) invoked.", userId, dto);
			
			// 1. TodoEntity로 변환한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// 2. id를 null로 초기화한다. 생성 당시에는 id가 없어야 하기 때문.
			entity.setId(null);

			// 3. 임시 사용자 아이디를 설정해준다.(4장 인증과 인가에서 수정할 예정)
			entity.setUserId(userId);
			
			// 4. 서비스를 이용하여 Todo Entity 생성
			List<TodoEntity> entities = service.create(entity);
			log.info("\t+ entities : {} ", Arrays.toString(entities.toArray()));
			// service.create를 사용하여 만든 entity가 비어있음
			 
			// 5. 자바 스트림을 이용하여 리턴된 엔티티 리스트 -> TodoDTO 리스트로 변환
			// 참조 표현식 클래스이름::메소드이름 => (entity) -> new TodoDTO(entity)와 동일하다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
//			List<TodoDTO> dtos = entities.stream().map((e) -> new TodoDTO(e)).collect(Collectors.toList());
			log.info("\t+ dtos : {}", Arrays.toString(dtos.toArray()));
			 
			// 6. 변환된 TodoDTO 리스트를 이용하여 ResponseDTO를 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			log.info("\t+ response : {}", response);
			
			// 7. ResponseEntity에 ResponseDTO를 담아 를 리턴한다.
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			// 8. 혹시 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		} // try-catch
	} // createTodo
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
		
		log.info("\t+ retrieveTodoList({}) invoked.", userId);
		
		// 1. 서비스 메서드의 retrieve() 메서드를 사용해 Todo 리스트 가져오기
		List<TodoEntity> entities = service.retrieve(userId);
		
		// 2. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
		List<TodoDTO> dtos = entities.stream().map((e) -> new TodoDTO(e)).collect(Collectors.toList());
		
		// 3. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// 4. ResponseDTO 리턴
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
										@RequestBody TodoDTO dto){
		
		log.info("\t+ updateTodo({}, {}) invoked.", userId, dto);
		
		// 1. dto를 entity로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// 2. id를 인증받은 아이디로 초기화
		entity.setUserId(userId);
		
		// 3. service를 이용하여 entity 업데이트
		List<TodoEntity> entities =  service.update(entity);
		
		// 4. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// 5. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// 6. ResponseDTO를 리턴
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,
										@RequestBody TodoDTO dto) {
		
		try {
			log.info("\t+ deleteTodo({}, {}) invoked.", userId, dto);
			
			// 1. TodoEntity로 변환
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// 2. 인증받은 사용자 아이디를 설정
			entity.setUserId(userId);
			
			// 3. 서비스를 이용해 entity 삭제
			List<TodoEntity> entites = service.delete(entity);
			
			// 4. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
			List<TodoDTO> dtos = entites.stream().map(TodoDTO::new).collect(Collectors.toList());
		
			// 5. 변환된 TodoDTO 리스트를 이용하여 ResponseDTO를 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// 6. ResponseDTO 리턴
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			// 예외가 있는 경우 dto 대신 error 메시지를 넣어 리턴
			String error = e.getMessage();
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
} // end class