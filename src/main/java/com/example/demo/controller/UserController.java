package com.example.demo.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
		
		try {
			
			log.info("\t+ registerUser({}) method invoked.", userDTO);
			// 요청을 이용해 저장할 사용자 만들기
			UserEntity user =  UserEntity.builder()
											.email(userDTO.getEmail())
											.username(userDTO.getUsername())
											.password(userDTO.getPassword())
											.build();
			
			// 서비스를 이용하여 repository에 사용자 저장
			UserEntity registeredUser = userService.create(user);
			
			UserDTO responseDTO = UserDTO.builder()
								.email(registeredUser.getEmail())
								.username(registeredUser.getUsername())
								.password(registeredUser.getPassword())
								.build();
			
			return ResponseEntity.ok().body(responseDTO);
		} catch (Exception e) {
			// 사용자 정보는 항상 하나이므로 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
			ResponseDTO<?> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	} // registerUser method
	
	// user의 인증받는 controller
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		
		log.info("\t+ authenticate({}) method invoked.", userDTO);
		
		UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword());
		
		if(user != null) {
			log.info("\t+ user authenticate success => user : {}", user);
			// 토큰 생성
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
												.email(user.getEmail())
												.id(user.getId())
												.token(token)
												.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			log.info("\t+ user authenticate failed => user : {}", user);
			ResponseDTO<?> responseDTO = ResponseDTO.builder().error("Login failed").build();

			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	

} // end class