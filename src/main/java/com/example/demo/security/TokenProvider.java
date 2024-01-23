package com.example.demo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenProvider {
	
	private static final String SECRET_KEY = "NMA8JPctFuna59f5";
//	private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	// Token 생성
	public String create(UserEntity userEntity) {
		
		log.info("\t+ create({}) method invoked", userEntity);
		
		Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
		
		return Jwts.builder()
					.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
					.setSubject(userEntity.getId()) // 유일한 식별자
					.setIssuer("Todo app made by nan-nong")
					.setIssuedAt(new Date())
					.setExpiration(expiryDate)
					.compact();
	} // token create
	
	// 생성한 토큰으로 디코딩 및 파싱하고 위조 여부 파악
	public String validateAndGetUserId(String token) {
		
		log.info("\t+ validateAndGetUserId({}) method invoked", token);

//		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//		Claims claims = Jwts.parserBuilder()
//							.setSigningKey(SECRET_KEY).build()
//							.parseClaimsJws(token).getBody();
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
		
		return claims.getSubject(); // 우리가 원하는 사용자id return
	}

} // end class