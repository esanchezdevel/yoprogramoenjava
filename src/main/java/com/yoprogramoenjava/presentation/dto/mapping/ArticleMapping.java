package com.yoprogramoenjava.presentation.dto.mapping;

import com.yoprogramoenjava.domain.model.Article;
import com.yoprogramoenjava.presentation.dto.ArticleDTO;

public class ArticleMapping {

	public static Article parseToEntity(ArticleDTO dto) {
		Article entity = new Article();
		entity.setTitle(dto.title());
		entity.setTopic(null); // TODO get topic object from database
		entity.setDescription(dto.description());
		entity.setContent(dto.content());
		
		return entity;
	}
}
