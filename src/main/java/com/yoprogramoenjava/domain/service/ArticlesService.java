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
}
