package com.programandoconjava.infrastructure.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programandoconjava.domain.model.ExternalNew;

public interface ExternalNewsRepository extends JpaRepository<ExternalNew, Long> {

	List<ExternalNew> findTop5ByOrderByDateCreationDesc();

	List<ExternalNew> findAllByOrderByDateCreationDesc();
}
