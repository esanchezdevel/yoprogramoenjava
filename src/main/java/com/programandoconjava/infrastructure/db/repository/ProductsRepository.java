package com.programandoconjava.infrastructure.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.programandoconjava.domain.model.Product;
import com.programandoconjava.infrastructure.db.dto.ProductDTO;

public interface ProductsRepository extends JpaRepository<Product, Long>{

	@Query("SELECT new com.programandoconjava.infrastructure.db.dto.ProductDTO(a.id, a.name, a.description, a.type) FROM Product a ORDER BY a.id DESC")
	List<ProductDTO> findAllProducts();
}
