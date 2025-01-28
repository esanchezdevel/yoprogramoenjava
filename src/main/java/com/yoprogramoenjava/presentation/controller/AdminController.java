package com.yoprogramoenjava.presentation.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yoprogramoenjava.application.utils.Constants;
import com.yoprogramoenjava.domain.service.ArticlesService;
import com.yoprogramoenjava.domain.service.TopicsService;
import com.yoprogramoenjava.presentation.dto.ArticleDTO;
import com.yoprogramoenjava.presentation.dto.TopicDTO;
import com.yoprogramoenjava.presentation.dto.mapping.ArticleMapping;
import com.yoprogramoenjava.presentation.dto.mapping.TopicMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final Logger logger = LogManager.getLogger(AdminController.class);
	
	@Autowired
	private ArticlesService articlesService;

	@Autowired
	private TopicsService topicsService;
	
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
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TOPICS, topicsService.getAll());

		return "admin/article_form";
	}
	
	@PostMapping("/articles/create")
	public String postCreateArticle(@ModelAttribute ArticleDTO articleDTO, Model model) {
		logger.info("Processing new article: {}", articleDTO);
		
		articlesService.store(ArticleMapping.parseToEntity(articleDTO, topicsService));
		
		return "admin/index";
	}
	
	@GetMapping("/topics")
	public String getTopics(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		model.addAttribute(Constants.ATTRIBUTE_NAME_TOPICS, topicsService.getAll());

		return "admin/topics";
	}

	@GetMapping("/topics/create")
	public String getCreateTopic(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		return "admin/topic_form";
	}

	@PostMapping("/topics/create")
	public String postCreateTopic(@ModelAttribute TopicDTO topicDTO, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		topicsService.store(TopicMapping.parseToEntity(topicDTO));

		return "admin/index";
	}
}
