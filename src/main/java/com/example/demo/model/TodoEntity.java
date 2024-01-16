package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 빌더 패턴 : 객체를 정의하고 그 객체를 생성할 때 보통 생성자를 통해 생성하는 것이지만, 
// 				객체를 생성하는 별도 builder를 두는 것을 빌더 패턴이라고 한다.
// 빌더 패턴을 사용하는 이유 : https://pamyferret.tistory.com/67
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity  // Entity임을 명시하기 위하여 추가
@Table(name = "Todo") // 데이터베이스의 Todo 테이블에 매핑된다.
public class TodoEntity {
	
	@Id		// 기본키가 될 필드에 지정
	@GeneratedValue(generator = "system-uuid")		// ID를 자동으로 생성하겠다는 어노테이션이며 @GenericGenerator에서 정의된 이름으로 사용할 수 있다 아래의 Generator를 참조해 사용한다.
	@GenericGenerator(name = "system-uuid", strategy = "uuid") // 나만의 Generator를 사용하고 싶은 경우 이용
	private String id;			// 오브젝트 아이디
	private String userId;		// 이 오브젝트를 생성한 사용자의 아이디
	private String title;		// Todo 타이틀
	private boolean done;		// true일시 todo를 완료(checked)

} 