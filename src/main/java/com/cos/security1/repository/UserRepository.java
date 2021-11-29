package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

//CRUD함수를 JpaRespository가 들고 있음. 
// @Respository라는 어노테이션이 없어도 Ioc가 된다.
//이유는 JpaRepository를 상속했기 때문이다. 자동으로 bean등록도 된다.
public interface UserRepository extends JpaRepository<User, Integer>{
	// findBy 규칙 -> Username문법
	// select * from user where username =1?
	User findByUsername(String username);   //Jpa query methods 검색

}
