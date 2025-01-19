package com.yoprogramoenjava.domain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoprogramoenjava.domain.model.Article;
import com.yoprogramoenjava.domain.service.ArticlesService;
import com.yoprogramoenjava.infrastructure.db.repository.ArticlesRepository;

@Service
public class ArticlesServiceImpl implements ArticlesService {

	@Autowired
	private ArticlesRepository articlesRepository;
	
	@Override
	public List<Article> getAll() {
		return articlesRepository.findAll();
	}
}
