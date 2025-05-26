package com.programandoconjava.presentation.dto.mapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.programandoconjava.domain.model.Currency;
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
		entity.setFilename(dto.filename());
		entity.setPrice(Float.parseFloat(dto.price()));
		entity.setCurrency(Currency.valueOf(dto.currency()));
		entity.setTax(Float.parseFloat(dto.tax()));
		
		return entity;
	}

	public static ProductDTO parseToDTO(Product entity) {

		String metaDescription = entity.getDescription().replace("<br>", " ").replace("\n", "").replace("\r", "");
		metaDescription = metaDescription.length() > 155 ? metaDescription.substring(0, 154) : metaDescription;

		DecimalFormat df = new DecimalFormat("#0.00");
		ProductDTO dto = new ProductDTO(String.valueOf(entity.getId()),
										entity.getName(),
										entity.getType().toString(),
										entity.getDescription(),
										metaDescription,
										entity.getPreviewImage(),
										entity.getPreviewVideo(),
										entity.getFilename(),
										df.format(entity.getPrice()),
										entity.getCurrency().value(),
										df.format(entity.getTax()));
		return dto;
	}

	public static List<ProductDTO> parseListToDTOs(List<Product> entities) {
		List<ProductDTO> dtos = new ArrayList<>();
		
		entities.forEach(e -> dtos.add(parseToDTO(e)));
		
		return dtos;
	}
}
