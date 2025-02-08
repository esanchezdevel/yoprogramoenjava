package com.yoprogramoenjava.domain.service;

import java.util.List;
import java.util.Optional;

import com.yoprogramoenjava.domain.model.Topic;

public interface TopicsService {

	/**
	 * Get all the topics from database
	 * 
	 * @return List of Topics
	 */
	public List<Topic> getAll();

	
	/**
	 * Get an specific topic from database looking by
	 * the title
	 * 
	 * @param title The title of the topic
	 * @return Optional of Topic. Empty if the topic is not found
	 */
	public Optional<Topic> getByTitle(String title);


	/**
	 * Store one Topic object in database
	 * 
	 * @param topic The topic to be stored
	 */
	public void store(Topic topic);
	

	/**
	 * Update one topic in database
	 * 
	 * @param id The topic id
	 * @param topic The topic to be updated
	 */
	void update(Long id, Topic topic);
}
