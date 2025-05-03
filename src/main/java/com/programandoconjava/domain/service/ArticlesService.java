package com.programandoconjava.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.programandoconjava.domain.model.Article;

public interface ArticlesService {

	/**
	 * Get all the Articles stored in database
	 * 
	 * @return List of Articles
	 */
	List<Article> getAll();

	/**
	 * Get all Articles with status published
	 * 
	 * @return List of Articles
	 */
	List<Article> getPublishedArticles();

	/**
	 * Get the last X published articles
	 * 
	 * @param numberOfArticles Number of articles to get
	 * @return The list of articles found
	 */
	List<Article> getLastArticles(int numberOfArticles);

	/**
	 * Get one Article looking by the Id
	 * 
	 * @param id The id of the Article
	 * @param parseToHtml True to indicate if we want to parse the content of the article to HTML format
	 * @return Optional of Article. Empty if the Article is not found
	 */
	Optional<Article> getById(long id, boolean parseToHtml);

	/**
	 * Retrieve all the unique tags from the articles
	 * 
	 * @return List of unique tags
	 */
	Set<String> getAllTags();

	/**
	 * Get all articles that have the specified tag
	 * 
	 * @param tag Tag to look for
	 * @return The list of articles found
	 */
	List<Article> getByTag(String tag);
	
	/**
	 * Store one Article in database
	 * 
	 * @param article The article to be saved
	 */
	void store(Article article);

	/**
	 * Change the status of one article to published
	 * 
	 * @param id The id of the article to be updated
	 */
	void publish(String id);

	/**
	 * Change the status of one article to unpublished
	 * 
	 * @param id The id of the article to be updated
	 */
	void unpublish(String id);

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
