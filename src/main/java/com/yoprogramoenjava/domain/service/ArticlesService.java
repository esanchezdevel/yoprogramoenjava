package com.yoprogramoenjava.domain.service;

import java.util.List;

import com.yoprogramoenjava.domain.model.Article;

public interface ArticlesService {

	/**
	 * Get all the articles stored in database
	 * 
	 * @return List of Articles
	 */
	public List<Article> getAll();
	
	/**
	 * Store one Article in database
	 * 
	 * @param article The article to be saved
	 */
	public void store(Article article);
}
