package com.programandoconjava.domain.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programandoconjava.domain.model.Article;
import com.programandoconjava.domain.service.ArticlesService;
import com.programandoconjava.domain.service.HtmlParserService;
import com.programandoconjava.infrastructure.db.dto.ArticleDTO;
import com.programandoconjava.infrastructure.db.repository.ArticlesRepository;

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

			String parsedDescription = htmlParserService.parseToHtml(dto.description());

			article.setDescription(parsedDescription);
			article.setDateCreation(dto.dateCreation());
			article.setPublished(dto.published());

			articles.add(article);
		});
		return articles;
	}

	@Override
	public List<Article> getPublishedArticles() {
		List<ArticleDTO> articlesDTOs = articlesRepository.findByPublished(true);
		
		List<Article> articles = new ArrayList<>();
		articlesDTOs.forEach(dto -> {
			Article article = new Article();
			article.setId(dto.id());
			article.setTitle(dto.title());

			String parsedDescription = htmlParserService.parseToHtml(dto.description());

			article.setDescription(parsedDescription);
			article.setTags(dto.tags());
			article.setDateCreation(dto.dateCreation());
			article.setPublished(dto.published());

			articles.add(article);
		});
		return articles;
	}

	@Override
	public Optional<Article> getById(long id, boolean parseToHtml) {
		Optional<Article> article = articlesRepository.findById(id);

		if (article.isEmpty()) {
			logger.warn("Article with id '{}' not found", id);
			return article;
		}

		String parsedDescription = parseToHtml ? htmlParserService.parseToHtml(article.get().getDescription()) : article.get().getDescription();
		String parsedContent = parseToHtml ? htmlParserService.parseToHtml(article.get().getContent()) : article.get().getContent();

		article.get().setDescription(parsedDescription);
		article.get().setContent(parsedContent);

		return article;
	}
	
	@Override
	public Set<String> getAllTags() {
		List<String> tags = articlesRepository.findAllTags(true);

		Set<String> uniqueTags = new HashSet<>();

		tags.forEach(articleTags -> {
			String[] articleTagsArray = articleTags.split(",");
			for (String tag : articleTagsArray) {
				uniqueTags.add(tag.trim());
			}
		});
		return uniqueTags;
	}

	@Override
	public List<Article> getByTag(String tag) {
		List<Article> allArticles = getPublishedArticles();

		List<Article> result = new ArrayList<>();

		for (Article a : allArticles) {
			if (isTagPresent(a.getTags(), tag)) {
				result.add(a);
				continue;
			}
		};

		return result;
	}

	private boolean isTagPresent(String allTags, String tag) {
		boolean found = false;
		String[] tagsArray = allTags.split(",");

		for (String t : tagsArray) {
			if (t.trim().equalsIgnoreCase(tag)) {
				found = true;
				break;
			}
		}
		return found;
	}

	@Override
	public void store(Article article) {
		articlesRepository.save(article);	
	}

	@Override
	public void publish(String id) {
		Optional<Article> article = articlesRepository.findById(Long.valueOf(id));
		if (article.isPresent()) {
			article.get().setPublished(true);
		}
	}

	@Override
	public void unpublish(String id) {
		Optional<Article> article = articlesRepository.findById(Long.valueOf(id));
		if (article.isPresent()) {
			article.get().setPublished(false);
		}
	}

	@Override
	@Transactional
	public void update(Long id, Article article) {
		
		Optional<Article> articleDb = articlesRepository.findById(id);

		if (articleDb.isPresent()) {
			articleDb.get().setTitle(article.getTitle());
			articleDb.get().setDescription(article.getDescription());
			articleDb.get().setTags(article.getTags());
			articleDb.get().setContent(article.getContent());
			articleDb.get().setTopic(article.getTopic());
		}
	}


	@Override
	public void delete(Long id) {
		articlesRepository.deleteById(id);
	}
}
