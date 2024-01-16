package com.example.demo.dto;

import com.example.demo.model.TodoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* DTO 생성 이유 
 * 	1. 비즈니스 로직 캡슐화 => model은 데이터베이스 테이블 구조와 매우 유사하기 때문에 외부인이 자사의 데이터베이스 스키마를 아는 것을 원치 않기 때문에 외부 사용자에게 서비스 내부 로직, 데이터베이스 구조를 숨기기 위해 
 * 	2. 클라이언트가 필요한 정보를 모델이 전부 포함하지 않는 경우 많음 => 서비스 실행 도중 에러메시지 발생 시 모델에 서비스 로직과 관련없어서 담기 애매하기에 dto에 담음*/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoDTO {
	
	private String id;
	private String title;
	private boolean done;
	
	public TodoDTO(final TodoEntity entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.done = entity.isDone();
	}

}