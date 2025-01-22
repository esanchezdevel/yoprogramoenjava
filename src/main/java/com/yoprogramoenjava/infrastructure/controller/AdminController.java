package com.yoprogramoenjava.infrastructure.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yoprogramoenjava.application.utils.Constants;
import com.yoprogramoenjava.domain.service.ArticlesService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final Logger logger = LogManager.getLogger(AdminController.class);
	
	@Autowired
	private ArticlesService articlesService;
	
	@GetMapping() 
	public String getAdminPanel(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "admin/index";
	}
	
	@GetMapping("/articles")
	public String getArticles(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLES, articlesService.getAll());
		
		return "admin/articles";
	}
	
	@GetMapping("/articles/create")
	public String createArticleForm(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "admin/article_form";
	}
	
	@PostMapping("/articles/create")
	public String postCreateArticle(Model model) {
		logger.info("Processing new article");
		return "admin/index";
	}
	
	
}
