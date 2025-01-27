package com.yoprogramoenjava.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yoprogramoenjava.domain.model.Topic;

public interface TopicsRepository extends JpaRepository<Topic, Long>{

}
