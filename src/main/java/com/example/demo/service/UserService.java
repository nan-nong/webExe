package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
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
	public UserEntity getByCredentials(final String email, final String password) {
		
		log.info("\t+ getByCredentials({}, {}) method invoked.", email, password);
		return userRepository.findByEmailAndPassword(email, password);
	}

} 