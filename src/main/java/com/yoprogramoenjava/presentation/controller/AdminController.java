package com.yoprogramoenjava.presentation.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.yoprogramoenjava.application.utils.Constants;
import com.yoprogramoenjava.domain.model.Article;
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
	public RedirectView postCreateArticle(@ModelAttribute ArticleDTO articleDTO, Model model) {
		logger.info("Processing new article: {}", articleDTO);
		
		articlesService.store(ArticleMapping.parseToEntity(articleDTO, topicsService));
		
		return new RedirectView("/admin");
	}

	@GetMapping("/articles/edit/{id}")
	public String editArticleForm(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return "error";
		}

		Optional<Article> article = articlesService.getById(Long.valueOf(id));

		if (article.isEmpty()) {
			logger.error("Error. Article with id '{}' not found", id);
			return "error";
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLE, ArticleMapping.parseToDTO(article.get()));
		model.addAttribute(Constants.ATTRIBUTE_NAME_TOPICS, topicsService.getAll());

		return "admin/article_edit_form";
	}

	@GetMapping("/articles/delete/{id}")
	public RedirectView deleteArticle(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return new RedirectView("/error");
		}

		articlesService.delete(Long.valueOf(id));
		logger.info("Article with id '{}' deleted", id);

		return new RedirectView("/admin/articles");
	}

	@PostMapping("/articles/edit/{id}")
	public RedirectView editArticle(@PathVariable String id, @ModelAttribute ArticleDTO articleDTO, Model model) {
		logger.info("Edit article with id: {}", id);

		articlesService.update(Long.valueOf(id), ArticleMapping.parseToEntity(articleDTO, topicsService));

		logger.info("Article modified");
		return new RedirectView("/admin");
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
	public RedirectView postCreateTopic(@ModelAttribute TopicDTO topicDTO, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		topicsService.store(TopicMapping.parseToEntity(topicDTO));

		return new RedirectView("/admin");
	}

	@PostMapping("/topics/edit/{id}")
	public RedirectView editTopic(@PathVariable String id, @ModelAttribute TopicDTO topicDTO, Model model) {
		logger.info("Edit topic with id: {}", id);

		topicsService.update(Long.valueOf(id), TopicMapping.parseToEntity(topicDTO));

		logger.info("Topic modified");
		return new RedirectView("/admin/topics");
	}
}
