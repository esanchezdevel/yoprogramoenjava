package com.programandoconjava.presentation.dto.mapping;

import org.springframework.util.StringUtils;

import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.model.ProductType;
import com.programandoconjava.presentation.dto.ProductDTO;

public class ProductMapping {

	public static Product parseToEntity(ProductDTO dto) {
		Product entity = new Product();
		entity.setName(dto.name());
		entity.setDescription(dto.description());
		entity.setType(ProductType.valueOf(dto.type()));
		entity.setDescription(dto.description());
		entity.setPreviewImage(dto.imagePreview());
		if (StringUtils.hasLength(dto.videoPreview()))
			entity.setPreviewVideo(dto.videoPreview());
		entity.setPrice(Double.parseDouble(dto.price())); 
		
		return entity;
	}

	public static ProductDTO parseToDTO(Product entity) {
		return parseToDTO(entity, true);
	}

	public static ProductDTO parseToDTO(Product entity, boolean isMetaDescription) {
		ProductDTO dto = new ProductDTO(String.valueOf(entity.getId()),
										entity.getName(),
										entity.getType().toString(),
										isMetaDescription ? entity.getDescription().replace("<br>", " ").replace("\n", "").replace("\r", "") : entity.getDescription(), 
										entity.getPreviewImage(),
										entity.getPreviewVideo(),
										String.valueOf(entity.getPrice()));
		return dto;
	}
}
