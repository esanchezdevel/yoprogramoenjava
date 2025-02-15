package com.yoprogramoenjava.presentation.dto.mapping;

import com.yoprogramoenjava.domain.model.ExternalNew;
import com.yoprogramoenjava.presentation.dto.ExternalNewDTO;

public class ExternalNewsMapping {

	public static ExternalNew parseToEntity(ExternalNewDTO dto) {
		ExternalNew entity = new ExternalNew();
		entity.setTitle(dto.title());
		entity.setSource(dto.source());
		entity.setLink(dto.link());
		
		return entity;
	}
}
