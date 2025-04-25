package com.programandoconjava.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programandoconjava.domain.model.StatBot;

public interface StatsBotsRepository extends JpaRepository<StatBot, Long>{

}
