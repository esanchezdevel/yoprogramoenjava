package com.programandoconjava.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programandoconjava.domain.model.Client;

public interface ClientsRepository extends JpaRepository<Client, Long>{

}
