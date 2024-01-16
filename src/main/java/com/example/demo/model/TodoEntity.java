package com.example.demo.model;

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
public class TodoEntity {
	
	private String id;			// 오브젝트 아이디
	private String userId;		// 이 오브젝트를 생성한 사용자의 아이디
	private String title;		// Todo 타이틀
	private boolean done;		// true일시 todo를 완료(checked)

} 