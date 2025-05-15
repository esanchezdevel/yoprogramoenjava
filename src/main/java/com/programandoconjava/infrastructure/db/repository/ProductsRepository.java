package com.programandoconjava.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programandoconjava.domain.model.Product;

public interface ProductsRepository extends JpaRepository<Product, Long>{

}
