package com.yoprogramoenjava.domain.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
	private String description;
	
	@CreatedDate
	@Column(name = "date_creation")
	private LocalDateTime dateCreation;
	
	@LastModifiedDate
	@Column(name = "date_last_update")
	private LocalDateTime dateLastUpdate;
	
	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Article> articles;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}

	public LocalDateTime getDateLastUpdate() {
		return dateLastUpdate;
	}

	public void setDateLastUpdate(LocalDateTime dateLastUpdate) {
		this.dateLastUpdate = dateLastUpdate;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", title=" + title + ", description=" + description + ", dateCreation="
				+ dateCreation + ", dateLastUpdate=" + dateLastUpdate + ", articles=" + articles.size() + "]";
	}
}
