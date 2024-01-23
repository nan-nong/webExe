package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity create(final UserEntity userEntity) {
		log.info("\t+ create({}) method invoked.", userEntity);
		
		if(userEntity == null || userEntity.getEmail() == null){
			throw new RuntimeException("Invalid arguments");
		}
		
		final String email = userEntity.getEmail();
		
		if(userRepository.existsById(email)) {
			log.warn("\t+ Email already exists {}", email);
			throw new RuntimeException("Email already exists");
		}
		
		return userRepository.save(userEntity);
	}
	
	// 자격 증명 얻기
	public UserEntity getByCredentials(	final String email, 
										final String password,
										final PasswordEncoder encoder ) {
		log.info("\t+ getByCredentials({}, {}, {}) method invoked.", email, password, encoder);
		
		// 이메일 체크
		final UserEntity originalUser =  userRepository.findByEmail(email);
		log.info("\t+ originalUser 정보 : {}, {}, {}", originalUser.getEmail(), originalUser.getPassword(), originalUser.getUsername());
		
		// 패스워드 체크
		if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
			log.info("\t+ 비밀번호 검증 일치");
			return originalUser;
		}	
		
		log.info("\t+ 비밀번호 검증 불일치");
		return null;
	} // getByCredentials

} // UserService