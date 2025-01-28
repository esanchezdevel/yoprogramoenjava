package com.yoprogramoenjava.presentation.dto.mapping;

import com.yoprogramoenjava.domain.model.Article;
import com.yoprogramoenjava.domain.service.TopicsService;
import com.yoprogramoenjava.presentation.dto.ArticleDTO;

public class ArticleMapping {

	public static Article parseToEntity(ArticleDTO dto, TopicsService topicsService) {
		Article entity = new Article();
		entity.setTitle(dto.title());
		entity.setTopic(topicsService.getByTitle(dto.topic()).orElse(null));
		entity.setDescription(dto.description());
		entity.setContent(dto.content());
		
		return entity;
	}
}
