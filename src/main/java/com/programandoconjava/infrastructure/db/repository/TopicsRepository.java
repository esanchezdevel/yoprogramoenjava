package com.programandoconjava.infrastructure.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programandoconjava.domain.model.Topic;

public interface TopicsRepository extends JpaRepository<Topic, Long>{

	Optional<Topic> findByTitle(String title);
}
