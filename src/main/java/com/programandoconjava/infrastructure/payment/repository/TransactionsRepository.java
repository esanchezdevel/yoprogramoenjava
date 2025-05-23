package com.programandoconjava.infrastructure.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programandoconjava.infrastructure.payment.model.Transaction;

public interface TransactionsRepository extends JpaRepository<Transaction, Long>{

	List<Transaction> findAllByOrderId(String orderId);
}
