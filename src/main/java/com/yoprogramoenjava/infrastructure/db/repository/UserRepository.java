package com.yoprogramoenjava.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yoprogramoenjava.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
