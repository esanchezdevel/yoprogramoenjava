package com.programandoconjava.infrastructure.db.dto;

import com.programandoconjava.domain.model.ProductType;

public record ProductDTO (Long id, String name, String description, ProductType type) { }
