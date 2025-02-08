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

	/**
	 * Update one article in database
	 * 
	 * @param id The article id
	 * @param article The article to be updated
	 */
	void update(Long id, Article article);

	/**
	 * Delete one Article in base of the id
	 * 
	 * @param id The identifier of the Article to be deleted
	 */
	void delete(Long id);
}
