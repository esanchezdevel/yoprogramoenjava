package com.programandoconjava.presentation.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.programandoconjava.application.utils.Constants;
import com.programandoconjava.domain.model.Article;
import com.programandoconjava.domain.model.ExternalNew;
import com.programandoconjava.domain.model.Topic;
import com.programandoconjava.domain.service.ArticlesService;
import com.programandoconjava.domain.service.ExternalNewsService;
import com.programandoconjava.domain.service.HtmlParserService;
import com.programandoconjava.domain.service.TopicsService;
import com.programandoconjava.presentation.dto.mapping.ArticleMapping;
import com.programandoconjava.presentation.dto.mapping.ArticleTagsMapping;
import com.programandoconjava.presentation.dto.mapping.ExternalNewsMapping;
import com.programandoconjava.presentation.dto.mapping.TopicMapping;

import jakarta.transaction.Transactional;

@Controller
public class FrontendController {

	private static final Logger logger = LogManager.getLogger(FrontendController.class);

	@Autowired
	private ArticlesService articlesService;

	@Autowired
	private ExternalNewsService externalNewsService;

	@Autowired
	private TopicsService topicsService;

	@Autowired
	private HtmlParserService htmlParserService;

	@GetMapping("/")
	public String getIndex(Model model) {
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<ExternalNew> lastExternalNews = externalNewsService.getLast();
		logger.info("LastExternalNews found: {}", lastExternalNews.size());
		model.addAttribute(Constants.ATTRIBUTE_NAME_EXTERNAL_NEWS, ExternalNewsMapping.parseToListOfDTO(lastExternalNews));
		
		Set<String> tags = articlesService.getAllTags();
		model.addAttribute(Constants.ATTRIBUTE_NAME_TAGS, ArticleTagsMapping.parseToSetOfDTOs(tags));

		return "index";
	}

	@GetMapping("/articles")
	public String getArticles(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<Article> articles = articlesService.getPublishedArticles();

		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLES, ArticleMapping.parseListToDTOs(articles));

		return "articles";
	}

	@GetMapping("/articles/{id}")
	public String getArticle(@PathVariable long id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		Optional<Article> article = articlesService.getById(id, true);
		
		if (article.isEmpty() || !article.get().isPublished()) {
			logger.error("Article with id '{}' not found", id);
			return "error_not_found";
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLE, ArticleMapping.parseToDTO(article.get()));

		return "article";
	}

	@GetMapping("/articles/tag/{tag}")
	public String getArticlesByTag(@PathVariable String tag, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<Article> articles = articlesService.getByTag(tag);

		if (articles == null || articles.isEmpty()) {
			logger.error("Articles not found for tag '{}'", tag);
			return "error_not_found";
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLES, ArticleMapping.parseListToDTOs(articles));

		return "articles";
	}

	@GetMapping("/news")
	public String getExternalNews(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<ExternalNew> externalNews = externalNewsService.getAll();

		model.addAttribute(Constants.ATTRIBUTE_NAME_EXTERNAL_NEWS, ExternalNewsMapping.parseToListOfDTO(externalNews));

		return "news";
	}

	@GetMapping("/topics")
	@Transactional
	public String getTopics(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<Topic> topics = topicsService.getAll();

		model.addAttribute(Constants.ATTRIBUTE_NAME_TOPICS, TopicMapping.parseToListOfDTO(htmlParserService, topics));

		return "topics";
	}

	@GetMapping("/topics/{id}")
	@Transactional
	public String getTopicArticles(@PathVariable Long id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		Optional<Topic> topic = topicsService.getById(id);

		if (topic.isEmpty()) {
			logger.error("Topic with id '{}' not found", id);
			return "error_not_found";
		}

		List<Article> articles = topic.get().getArticles().stream().filter(a -> a.isPublished()).toList();

		model.addAttribute(Constants.ATTRIBUTE_NAME_TOPIC, TopicMapping.parseToDTO(topic.get()));
		model.addAttribute(Constants.ATTRIBUTE_NAME_ARTICLES, ArticleMapping.parseListToDTOs(htmlParserService, articles));

		return "topic";
	}

	@GetMapping("/about")
	public String getAbout(Model model) {
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "about";
	}

	@GetMapping("/cookies-policy")
	public String getCookiesPolicy(Model model) {
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "cookies_policy";
	}
}
