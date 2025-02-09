package com.yoprogramoenjava.infrastructure.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yoprogramoenjava.domain.model.User;

public interface UsersRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
}
