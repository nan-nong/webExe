package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain)
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
						new UsernamePasswordAuthenticationToken(userId, // 인증된 사용자의 정보(보통 UserDetails를 넣는다)
																null, 
																AuthorityUtils.NO_AUTHORITIES);
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				// SecurityContext는 SecurityContextHolder의 createEmptyContext()메서드를 이용해 생성할 수 있다. 생성한 컨텍스트에 인증 정보인 authentication을 넣고 다시 SecurityContextHolder에 컨텍스트로 등록하는 것. SecurityContextHolder는 기본적으로 ThreadLocal에 저장된다. => Thread마다 하나의 컨텍스트를 관리할 수 있다. 같은 스레드 내라면 어디에서든 접근 가능.
				securityContext.setAuthentication(authentication);
				SecurityContextHolder.setContext(securityContext);
			}
		} catch (Exception e) {
			logger.error("Could not set user authentication in security context", e);
		}
		
		filterChain.doFilter(request, response);
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