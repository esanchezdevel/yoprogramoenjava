package com.programandoconjava.presentation.dto.mapping;

import java.util.ArrayList;
import java.util.List;

import com.programandoconjava.domain.model.Article;
import com.programandoconjava.domain.service.HtmlParserService;
import com.programandoconjava.domain.service.TopicsService;
import com.programandoconjava.presentation.dto.ArticleDTO;

public class ArticleMapping {

	public static Article parseToEntity(ArticleDTO dto, TopicsService topicsService) {
		Article entity = new Article();
		entity.setTitle(dto.title());
		entity.setTags(dto.tags());
		entity.setTopic(topicsService.getByTitle(dto.topic()).orElse(null));
		entity.setDescription(dto.description());
		entity.setContent(dto.content());
		
		return entity;
	}

	public static ArticleDTO parseToDTO(Article entity) {
		return parseToDTO(entity, true);
	}

	public static ArticleDTO parseToDTO(Article entity, boolean isMetaDescription) {
		ArticleDTO dto = new ArticleDTO(String.valueOf(entity.getId()),
										entity.getTitle(), 
										entity.getTags(),
										entity.getTopic() != null ? entity.getTopic().getTitle() : "",
										isMetaDescription ? entity.getDescription().replace("<br>", " ").replace("\n", "").replace("\r", "") : entity.getDescription(), 
										entity.getContent(),
										entity.getDateCreation().toLocalDate().toString());
		return dto;
	}

	public static ArticleDTO parseToDTO(HtmlParserService htmlParserService, Article entity) {
		ArticleDTO dto = new ArticleDTO(String.valueOf(entity.getId()),
										entity.getTitle(), 
										entity.getTags(),
										entity.getTopic() != null ? entity.getTopic().getTitle() : "", 
										htmlParserService.parseToHtml(entity.getDescription()), 
										htmlParserService.parseToHtml(entity.getContent()),
										entity.getDateCreation().toLocalDate().toString());
		return dto;
	}

	public static List<ArticleDTO> parseListToDTOs(List<Article> entities) {
		List<ArticleDTO> dtos = new ArrayList<>();
		
		entities.forEach(e -> dtos.add(parseToDTO(e, false)));
		
		return dtos;
	}
	
	public static List<ArticleDTO> parseListToDTOs(HtmlParserService htmlParserService, List<Article> entities) {
		List<ArticleDTO> dtos = new ArrayList<>();
		
		entities.forEach(e -> dtos.add(parseToDTO(htmlParserService, e)));
		
		return dtos;
	}
}
