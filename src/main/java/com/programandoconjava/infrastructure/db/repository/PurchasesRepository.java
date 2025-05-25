package com.programandoconjava.infrastructure.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programandoconjava.domain.model.Purchase;

public interface PurchasesRepository extends JpaRepository<Purchase, Long> {

	Optional<Purchase> findByProductIdAndToken(Long productId, String token);
}
