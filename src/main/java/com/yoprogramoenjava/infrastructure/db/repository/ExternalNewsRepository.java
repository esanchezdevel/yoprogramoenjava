package com.yoprogramoenjava.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yoprogramoenjava.domain.model.ExternalNew;

public interface ExternalNewsRepository extends JpaRepository<ExternalNew, Long> {

}
