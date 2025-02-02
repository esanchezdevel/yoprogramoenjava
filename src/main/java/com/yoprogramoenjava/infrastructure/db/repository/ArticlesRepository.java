package com.yoprogramoenjava.infrastructure.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yoprogramoenjava.domain.model.Article;
import com.yoprogramoenjava.infrastructure.db.dto.ArticleDTO;

public interface ArticlesRepository extends JpaRepository<Article, Long> {

	@Query("SELECT new com.yoprogramoenjava.infrastructure.db.dto.ArticleDTO(a.id, a.title, a.description, a.dateCreation) FROM Article a ORDER BY a.dateCreation DESC")
	List<ArticleDTO> findAllArticles();
}
