package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			// 요청에서 토큰 가져오기
			String token = parseBearerToken(request);

			log.info("\t+ ***** Filter is running");
			
			// 토큰 검사. JWT이므로 인가 서버에 요청하지 않고도 검증 가능
			if(token != null && !token.equalsIgnoreCase("null")) {
				// userId 가져오기 => 위조된 경우 예외 처리
				String userId = tokenProvider.validateAndGetUserId(token);
				log.info("Authenticated user ID : {}", userId);
				
				//인증 완료 SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
				AbstractAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
				
			}
			
		} catch (Exception e) {
			
		}
		
	}
	
	private String parseBearerToken(HttpServletRequest req) {
		// Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴
		log.info("\t+ parseBearerToken({}) invoked.", req);
		String bearerToken = req.getHeader("Authorization");
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

} // end class