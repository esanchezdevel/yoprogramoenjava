package com.yoprogramoenjava.presentation.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.yoprogramoenjava.application.utils.Constants;
import com.yoprogramoenjava.domain.model.Article;
import com.yoprogramoenjava.domain.model.ExternalNew;
import com.yoprogramoenjava.domain.service.ArticlesService;
import com.yoprogramoenjava.domain.service.ExternalNewsService;
import com.yoprogramoenjava.presentation.dto.ExternalNewDTO;
import com.yoprogramoenjava.presentation.dto.mapping.ArticleMapping;
import com.yoprogramoenjava.presentation.dto.mapping.ExternalNewsMapping;

@Controller
public class FrontendController {

	private static final Logger logger = LogManager.getLogger(FrontendController.class);

	@Autowired
	private ArticlesService articlesService;

	@Autowired
	private ExternalNewsService externalNewsService;

	@GetMapping("/")
	public String getIndex(Model model) {
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "index";
	}

	@GetMapping("/articles")
	public String getArticles(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<Article> articles = articlesService.getAll();

		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLES, ArticleMapping.parseListToDTOs(articles));

		return "articles";
	}

	@GetMapping("/articles/{id}")
	public String getArticle(@PathVariable long id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		Optional<Article> article = articlesService.getById(id, true);
		
		if (article.isEmpty()) {
			logger.error("Article with id '{}' not found", id);
			return "error_not_found";
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLE, ArticleMapping.parseToDTO(article.get()));

		return "article";
	}

	@GetMapping("/news")
	public String getExternalNews(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<ExternalNew> externalNews = externalNewsService.getAll();

		model.addAttribute(Constants.ATTRIBUTE_NAME_EXTERNAL_NEWS, ExternalNewsMapping.parseToListOfDTO(externalNews));

		return "news";
	}
}
