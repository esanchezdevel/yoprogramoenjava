package com.programandoconjava.presentation.controller;

import java.util.List;
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

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.application.utils.Constants;
import com.programandoconjava.domain.model.Article;
import com.programandoconjava.domain.model.Currency;
import com.programandoconjava.domain.model.ExternalNew;
import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.model.ProductType;
import com.programandoconjava.domain.model.Topic;
import com.programandoconjava.domain.service.ArticlesService;
import com.programandoconjava.domain.service.ExternalNewsService;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.domain.service.TopicsService;
import com.programandoconjava.presentation.dto.ArticleDTO;
import com.programandoconjava.presentation.dto.ExternalNewDTO;
import com.programandoconjava.presentation.dto.ProductDTO;
import com.programandoconjava.presentation.dto.TopicDTO;
import com.programandoconjava.presentation.dto.mapping.ArticleMapping;
import com.programandoconjava.presentation.dto.mapping.ExternalNewsMapping;
import com.programandoconjava.presentation.dto.mapping.ProductMapping;
import com.programandoconjava.presentation.dto.mapping.TopicMapping;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final Logger logger = LogManager.getLogger(AdminController.class);
	
	@Autowired
	private ArticlesService articlesService;

	@Autowired
	private TopicsService topicsService;

	@Autowired
	private ExternalNewsService externalNewsService;

	@Autowired
	private ProductsService productsService;
	
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
	@Transactional
	public RedirectView postCreateArticle(@ModelAttribute ArticleDTO articleDTO, Model model) {
		logger.info("Processing new article: {}", articleDTO);
		
		articlesService.store(ArticleMapping.parseToEntity(articleDTO, topicsService));
		
		return new RedirectView("/admin");
	}

	@GetMapping("/articles/publish/{id}")
	@Transactional
	public RedirectView postPublishArticle(@PathVariable String id, Model model) {
		logger.info("Publish article: {}", id);
		
		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return new RedirectView("/error");
		}

		articlesService.publish(id);
		articlesService.cleanLastArticlesList();
		
		return new RedirectView("/admin/articles");
	}

	@GetMapping("/articles/unpublish/{id}")
	@Transactional
	public RedirectView postUnpublishArticle(@PathVariable String id, Model model) {
		logger.info("Unpublish article: {}", id);
		
		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return new RedirectView("/error");
		}

		articlesService.unpublish(id);
		articlesService.cleanLastArticlesList();
		
		return new RedirectView("/admin/articles");
	}

	@GetMapping("/articles/edit/{id}")
	public String editArticleForm(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return "error";
		}

		Optional<Article> article = articlesService.getById(Long.valueOf(id), false);

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
			return new RedirectView("/admin/error");
		}

		articlesService.cleanLastArticlesList();
		articlesService.delete(Long.valueOf(id));
		logger.info("Article with id '{}' deleted", id);

		return new RedirectView("/admin/articles");
	}

	@PostMapping("/articles/edit/{id}")
	@Transactional
	public RedirectView editArticle(@PathVariable String id, @ModelAttribute ArticleDTO articleDTO, Model model) {
		logger.info("Edit article with id: {}", id);

		articlesService.update(Long.valueOf(id), ArticleMapping.parseToEntity(articleDTO, topicsService));

		articlesService.cleanLastArticlesList();

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

	@GetMapping("/topics/edit/{id}")
	public String editTopicsForm(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return "error";
		}

		Optional<Topic> topic = topicsService.getById(Long.valueOf(id));

		if (topic.isEmpty()) {
			logger.error("Error. Topic with id '{}' not found", id);
			return "error";
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_TOPIC, TopicMapping.parseToDTO(topic.get()));

		return "admin/topic_edit_form";
	}

	@GetMapping("/topics/delete/{id}")
	public RedirectView deleteTopic(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return new RedirectView("/admin/error");
		}

		topicsService.delete(Long.valueOf(id));
		logger.info("Topic with id '{}' deleted", id);

		return new RedirectView("/admin/topics");
	}

	@GetMapping("/news")
	public String getExternalNews(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		try {
			List<ExternalNew> externalNews = externalNewsService.getAll();
			model.addAttribute(Constants.ATTRIBUTE_NAME_EXTERNAL_NEWS, externalNews);
		} catch (AppException e) {
			logger.error("Error getting ExternalNews from Database", e);
			return "error";
		}
		return "admin/external_news.html";
	}

	@GetMapping("/news/create")
	public String createExternalNewsForm(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		return "admin/external_news_form";
	}

	@PostMapping("/news/create")
	public RedirectView postCreateExternalNews(@ModelAttribute ExternalNewDTO externalNewDTO, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (externalNewDTO == null || !StringUtils.hasLength(externalNewDTO.title()) ||
			!StringUtils.hasLength(externalNewDTO.source()) || !StringUtils.hasLength(externalNewDTO.link())) {
			logger.error("Error. Mandatory parameters are empty in dto: {}", externalNewDTO);
			return new RedirectView("/admin/error");
		}

		externalNewsService.store(ExternalNewsMapping.parseToEntity(externalNewDTO));

		return new RedirectView("/admin/news");
	}

	@GetMapping("/news/edit/{id}")
	public String editExternalNewsForm(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return "error";
		}

		try {
			Optional<ExternalNew> externalNew = externalNewsService.getById(Long.valueOf(id));

			if (externalNew.isEmpty()) {
				logger.error("Error. ExternalNew with id '{}' not found", id);
				return "error";
			}
			model.addAttribute(Constants.ATTRIBUTE_NAME_EXTERNAL_NEW, ExternalNewsMapping.parseToDTO(externalNew.get()));
		} catch (AppException e) {
			logger.error(e.getMessage());
			return "error";
		}

		return "admin/external_news_edit_form";
	}

	@PostMapping("/news/edit/{id}")
	public RedirectView editExternalNews(@PathVariable String id, @ModelAttribute ExternalNewDTO externalNewDTO, Model model) {
		logger.info("Edit externalNew with id: {}", id);

		externalNewsService.update(Long.valueOf(id), ExternalNewsMapping.parseToEntity(externalNewDTO));

		logger.info("ExternalNew modified");
		return new RedirectView("/admin/news");
	}

	@GetMapping("/news/delete/{id}")
	public RedirectView deleteExternalNews(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return new RedirectView("/admin/error");
		}

		externalNewsService.delete(Long.valueOf(id));
		logger.info("ExternalNew with id '{}' deleted", id);

		return new RedirectView("/admin/news");
	}

	@GetMapping("/products")
	public String getProducts(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_PRODUCTS, productsService.getAll());
		
		return "admin/products";
	}

	@GetMapping("/products/create")
	public String createProductsForm(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		model.addAttribute(Constants.ATTRIBUTE_NAME_PRODUCTS_TYPES, ProductType.values());
		model.addAttribute(Constants.ATTRIBUTE_NAME_CURRENCIES, Currency.values());

		return "admin/product_form";
	}
	
	@PostMapping("/products/create")
	@Transactional
	public RedirectView postCreateProduct(@ModelAttribute ProductDTO productDTO, Model model) {
		logger.info("Processing new product: {}", productDTO);
		
		productsService.store(ProductMapping.parseToEntity(productDTO));
		
		return new RedirectView("/admin");
	}

	@GetMapping("/products/edit/{id}")
	public String editProductForm(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return "error";
		}

		Optional<Product> product = productsService.getById(Long.valueOf(id), false);

		if (product.isEmpty()) {
			logger.error("Error. Product with id '{}' not found", id);
			return "error";
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_PRODUCT, ProductMapping.parseToDTO(product.get()));
		model.addAttribute(Constants.ATTRIBUTE_NAME_PRODUCTS_TYPES, ProductType.names());
		model.addAttribute(Constants.ATTRIBUTE_NAME_CURRENCIES, Currency.values());

		return "admin/product_edit_form";
	}

	@PostMapping("/products/edit/{id}")
	@Transactional
	public RedirectView editProduct(@PathVariable String id, @ModelAttribute ProductDTO productDTO, Model model) {
		logger.info("Edit product with id: {}", id);

		productsService.update(Long.valueOf(id), ProductMapping.parseToEntity(productDTO));

		logger.info("Product modified");
		return new RedirectView("/admin");
	}

	@GetMapping("/products/delete/{id}")
	public RedirectView deleteProduct(@PathVariable String id, Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		if (!StringUtils.hasLength(id)) {
			logger.error("Error. Empty ID received");
			return new RedirectView("/admin/error");
		}

		productsService.delete(Long.valueOf(id));
		logger.info("Product with id '{}' deleted", id);

		return new RedirectView("/admin/products");
	}
}
