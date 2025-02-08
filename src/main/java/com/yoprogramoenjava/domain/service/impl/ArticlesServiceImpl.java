package com.yoprogramoenjava.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoprogramoenjava.domain.model.Article;
import com.yoprogramoenjava.domain.service.ArticlesService;
import com.yoprogramoenjava.domain.service.HtmlParserService;
import com.yoprogramoenjava.infrastructure.db.dto.ArticleDTO;
import com.yoprogramoenjava.infrastructure.db.repository.ArticlesRepository;

import jakarta.transaction.Transactional;

@Service
public class ArticlesServiceImpl implements ArticlesService {

	private static final Logger logger = LogManager.getLogger(ArticlesServiceImpl.class);

	@Autowired
	private ArticlesRepository articlesRepository;

	@Autowired
	private HtmlParserService htmlParserService;
	
	@Override
	public List<Article> getAll() {

		List<ArticleDTO> articlesDTOs = articlesRepository.findAllArticles();
		
		List<Article> articles = new ArrayList<>();
		articlesDTOs.forEach(dto -> {
			Article article = new Article();
			article.setId(dto.id());
			article.setTitle(dto.title());
			article.setDescription(dto.description());
			article.setDateCreation(dto.dateCreation());

			articles.add(article);
		});
		return articles;
	}

	@Override
	public Optional<Article> getById(long id) {
		Optional<Article> article = articlesRepository.findById(id);

		if (article.isEmpty()) {
			logger.warn("Article with id '{}' not found", id);
			return article;
		}

		String parsedContent = htmlParserService.parseToHtml(article.get().getContent());

		article.get().setContent(parsedContent);

		return article;
	}
	

	@Override
	public void store(Article article) {
		articlesRepository.save(article);	
	}


	@Override
	@Transactional
	public void update(Long id, Article article) {
		
		Optional<Article> articleDb = articlesRepository.findById(id);

		if (articleDb.isPresent()) {
			articleDb.get().setTitle(article.getTitle());
			articleDb.get().setDescription(article.getDescription());
			articleDb.get().setContent(article.getContent());
			articleDb.get().setTopic(article.getTopic());
		}
	}


	@Override
	public void delete(Long id) {
		articlesRepository.deleteById(id);
	}
}
