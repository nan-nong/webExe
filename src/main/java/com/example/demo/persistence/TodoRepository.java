package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TodoEntity;

/* 1번째 매개변수 T : 테이블에 매핑될 엔티티 클래스(TodoEntity)
 * 2번째 매개변수 V : 엔티티의 기본 키의 타입(String)*/
//@Repository
//public interface TodoRepository extends JpaRepository<TodoEntity, String> {
//	
//	List<TodoEntity> findUserById(String userId);	// 스프링 데이터 JPA가 메서드 이름을 파싱해서 "SELECT * FROM TodoRepository WHERE userId = {userId}" 와 같은 쿼리를 작성한다.
//	
//}

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String>{
	
	List<TodoEntity> findByUserId(String userId);
}
// https://www.inflearn.com/questions/438046/userrepository-findbyuserid-userid-%EA%B5%AC%EB%AC%B8-%EA%B4%80%EB%A0%A8%ED%95%B4%EC%84%9C-%EC%A7%88%EB%AC%B8%EC%9D%B4%EC%9E%88%EC%8A%B5%EB%8B%88%EB%8B%A4

/* 
 * 안됐던 충격적인 이유.. findByUserId와 findUserById... 헷깔림..
*/