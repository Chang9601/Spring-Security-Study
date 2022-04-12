package com.cos.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security.model.User;

// CRUD 함수가 JpaRepository에 존재
// @Repository라는 어노테이션 없어도 IoC 가능, JpaRepository를 구현했기 때문
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// findBy 규칙
	// SELECT * FROM user WHERE username = 1?
	public User findByUsername(String username); // JPA Query Methods
}