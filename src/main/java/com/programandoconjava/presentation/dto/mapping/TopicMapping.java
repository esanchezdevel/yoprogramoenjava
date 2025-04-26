package com.programandoconjava.presentation.dto.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.programandoconjava.domain.model.Article;
import com.programandoconjava.domain.model.Topic;
import com.programandoconjava.domain.service.HtmlParserService;
import com.programandoconjava.presentation.dto.TopicDTO;

public class TopicMapping {

	public static Topic parseToEntity(TopicDTO dto) {
		Topic entity = new Topic();
		entity.setTitle(dto.title());
		entity.setDescription(dto.description());
		
		return entity;
	}

	public static TopicDTO parseToDTO(Topic entity) {
		String articles = entity.getArticles() != null ? String.valueOf(entity.getArticles().size()) : "0";
		return new TopicDTO(String.valueOf(entity.getId()), 
							entity.getTitle(), 
							entity.getDescription().replace("<br>", "").replace("\n", "").replace("\r", ""),
							articles);
	}

	public static TopicDTO parseToDTO(HtmlParserService htmlParserService, Topic entity) {
		List<Article> publishedArticles = entity.getArticles().stream().filter(a -> a.isPublished()).collect(Collectors.toList());
		String articles = publishedArticles != null ? String.valueOf(publishedArticles.size()) : "0";
		return new TopicDTO(String.valueOf(entity.getId()), 
							entity.getTitle(), 
							htmlParserService.parseToHtml(entity.getDescription()),
							articles);
	}

	public static List<TopicDTO> parseToListOfDTO(HtmlParserService htmlParserService, List<Topic> entities) {
		List<TopicDTO> dtos = new ArrayList<>();
		entities.forEach(entity -> dtos.add(parseToDTO(htmlParserService, entity)));
		return dtos;
	}
}
