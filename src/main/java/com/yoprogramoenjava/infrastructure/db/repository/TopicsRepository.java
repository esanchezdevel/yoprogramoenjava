package com.yoprogramoenjava.infrastructure.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yoprogramoenjava.domain.model.Topic;

public interface TopicsRepository extends JpaRepository<Topic, Long>{

	Optional<Topic> findByTitle(String title);
}
