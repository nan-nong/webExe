package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // 서비스 레이어임을 알려주는 어노테이션
public class TodoService {
	
	@Autowired
	private TodoRepository repository;
	
	public String testService() {
			
		// 1. TodoEntity를 생성한다.
		TodoEntity entity = TodoEntity.builder().title("My first Todo List").build();
		log.info("\t+ entity : {} ", entity);
		
		// 2. TodoEntity를 repository에 저장한다.
		repository.save(entity);
		log.info("\t+ respository entity save complete");
		
		// 3. TodoEntity를 Id로 repository에서 검색 한 다음 가져온다.
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		log.info("\t+ savedEntity : {} ", savedEntity);
		
		// 4. 저장된 Entity의 title return
		return savedEntity.getTitle();
	}
	
	/* create(생성) method */
	public List<TodoEntity> create(final TodoEntity entity) {
		
		validate(entity);
		
		repository.save(entity);
		log.info("\t+ Entity id : {} is saved", entity.getId());
		
		return repository.findByUserId(entity.getUserId());
	}
	
	/* retrieve(검색) method */
	public List<TodoEntity> retrieve(final String userId){
		
		log.info("\t+ retrieve({}) invoked.", userId);
		
		return repository.findByUserId(userId);
	}
	
	/* update(업데이트) method */
	public List<TodoEntity> update(final TodoEntity entity){
		
		// 1. 저장한 엔티티가 유효한지 확인하여 미리 구현해놓았던 validate()메소드 사용
		validate(entity);
		
		// 2. 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 불가
		Optional<TodoEntity> original = repository.findById(entity.getId());
		
//		// ifPresent : Optional 객체가 값을 가지고 있으면 실행 값이 없으면 넘어감
//		original.ifPresent(todo -> {
//			// 3. 반환된 TodoEntity가 존재하면 값을 새 entity 값으로 덮어 씌운다
//			todo.setTitle(entity.getTitle());
//			todo.setDone(entity.isDone());
//			
//			// 4. 데이터베이스에 새 값 저장
//			repository.save(todo);
//		});
		
		if(original.isPresent()) {
			final TodoEntity todo = original.get();
			
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			repository.save(todo);
		}

		// Retrieve Todo에서 만든 메서드를 이용해 사용자의 모든 Todo 리스트를 리턴
		return retrieve(entity.getUserId()); 
	}
	
	/* delete method */
	public List<TodoEntity> delete(final TodoEntity entity){
		
		// 1. 저장할 엔티티 유효한지 확인
		validate(entity);
		
		try {
			// 2. 엔티티 삭제
			repository.delete(entity);
		} catch (Exception e) {
			// 3. exception 발생 시 id와 exception 로깅
			log.error("error deleting entity", entity.getId(), e);
			
			// 4. 컨트롤러로 exception 보냄. 데이터 내부 로직을 캡슐화 하려면 e를 리턴하지 않고 새 exception 오브젝트를 리턴함
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		// 5. 새 Todo 리스트를 가져와 리턴
		return retrieve(entity.getUserId());
	}
	
	/* validate method */
	private void validate(final TodoEntity entity) {
		
		if(entity == null) {
			log.warn("entity cannot be null");
			
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId() == null) {
			log.warn("unknown user");
			
			throw new RuntimeException("unknown user.");
		}
		
	} // validate method
	
} // end class