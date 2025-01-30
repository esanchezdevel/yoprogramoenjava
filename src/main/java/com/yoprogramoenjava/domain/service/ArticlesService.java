package com.yoprogramoenjava.domain.service;

import java.util.List;
import java.util.Optional;

import com.yoprogramoenjava.domain.model.Article;

public interface ArticlesService {

	/**
	 * Get all the Articles stored in database
	 * 
	 * @return List of Articles
	 */
	List<Article> getAll();

	/**
	 * Get one Article looking by the Id
	 * 
	 * @param id The id of the Article
	 * @return Optional of Article. Empty if the Article is not found
	 */
	Optional<Article> getById(long id);
	
	/**
	 * Store one Article in database
	 * 
	 * @param article The article to be saved
	 */
	void store(Article article);
}
