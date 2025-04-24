package com.programandoconjava.presentation.dto.mapping;

import java.util.ArrayList;
import java.util.List;

import com.programandoconjava.domain.model.ExternalNew;
import com.programandoconjava.presentation.dto.ExternalNewDTO;

public class ExternalNewsMapping {

	public static ExternalNew parseToEntity(ExternalNewDTO dto) {
		ExternalNew entity = new ExternalNew();
		entity.setTitle(dto.title());
		entity.setSource(dto.source());
		entity.setLink(dto.link());
		
		return entity;
	}

	public static ExternalNewDTO parseToDTO(ExternalNew entity) {
		return new ExternalNewDTO(String.valueOf(entity.getId()), entity.getTitle(), entity.getSource(), entity.getLink());
	}

	public static List<ExternalNewDTO> parseToListOfDTO(List<ExternalNew> entities) {
		List<ExternalNewDTO> dtos = new ArrayList<ExternalNewDTO>();

		entities.forEach(entity -> dtos.add(parseToDTO(entity)));

		return dtos;
	}
}
