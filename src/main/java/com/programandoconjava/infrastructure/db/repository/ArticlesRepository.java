package com.programandoconjava.infrastructure.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programandoconjava.domain.model.Article;
import com.programandoconjava.infrastructure.db.dto.ArticleDTO;

public interface ArticlesRepository extends JpaRepository<Article, Long> {

	@Query("SELECT new com.programandoconjava.infrastructure.db.dto.ArticleDTO(a.id, a.title, a.description, a.tags, a.dateCreation, a.published) FROM Article a ORDER BY a.dateCreation DESC")
	List<ArticleDTO> findAllArticles();

	@Query("SELECT new com.programandoconjava.infrastructure.db.dto.ArticleDTO(a.id, a.title, a.description, a.tags, a.dateCreation, a.published) FROM Article a WHERE a.published = :published ORDER BY a.dateCreation DESC")
	List<ArticleDTO> findByPublished(@Param("published") boolean published);

	@Query("SELECT a.tags FROM Article a WHERE a.published = :published")
	List<String> findAllTags(@Param("published") boolean published);
}
