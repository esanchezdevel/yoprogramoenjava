package com.yoprogramoenjava.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yoprogramoenjava.domain.model.Stat;

public interface StatsRepository extends JpaRepository<Stat, Long>{

}
