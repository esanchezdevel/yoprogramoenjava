package com.yoprogramoenjava.presentation.dto.mapping;

import com.yoprogramoenjava.domain.model.Topic;
import com.yoprogramoenjava.presentation.dto.TopicDTO;

public class TopicMapping {

	public static Topic parseToEntity(TopicDTO dto) {
		Topic entity = new Topic();
		entity.setTitle(dto.title());
		entity.setDescription(dto.description());
		
		return entity;
	}
}
