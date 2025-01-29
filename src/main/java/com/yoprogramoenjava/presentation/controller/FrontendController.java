package com.yoprogramoenjava.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yoprogramoenjava.application.utils.Constants;
import com.yoprogramoenjava.domain.model.Article;
import com.yoprogramoenjava.domain.service.ArticlesService;
import com.yoprogramoenjava.presentation.dto.mapping.ArticleMapping;

@Controller
public class FrontendController {

	@Autowired
	private ArticlesService articlesService;

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
}
