package com.yoprogramoenjava.presentation.dto.mapping;

import java.util.ArrayList;
import java.util.List;

import com.yoprogramoenjava.domain.model.Topic;
import com.yoprogramoenjava.presentation.dto.TopicDTO;

public class TopicMapping {

	public static Topic parseToEntity(TopicDTO dto) {
		Topic entity = new Topic();
		entity.setTitle(dto.title());
		entity.setDescription(dto.description());
		
		return entity;
	}

	public static TopicDTO parseToDTO(Topic entity) {
		String articles = entity.getArticles() != null ? String.valueOf(entity.getArticles().size()) : "0";
		return new TopicDTO(String.valueOf(entity.getId()), entity.getTitle(), entity.getDescription(), articles);
	}

	public static List<TopicDTO> parseToListOfDTO(List<Topic> entities) {
		List<TopicDTO> dtos = new ArrayList<>();
		entities.forEach(entity -> dtos.add(parseToDTO(entity)));
		return dtos;
	}
}
