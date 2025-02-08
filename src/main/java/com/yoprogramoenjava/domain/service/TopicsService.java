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
	List<Topic> getAll();


	/**
	 * Get a Topic looking by the id
	 * 
	 * @param id The id of the topic to be retrieved
	 * @return Optional of the Topic found on database
	 */
	Optional<Topic> getById(Long id);
	
	/**
	 * Get an specific topic from database looking by
	 * the title
	 * 
	 * @param title The title of the topic
	 * @return Optional of Topic. Empty if the topic is not found
	 */
	Optional<Topic> getByTitle(String title);


	/**
	 * Store one Topic object in database
	 * 
	 * @param topic The topic to be stored
	 */
	void store(Topic topic);
	

	/**
	 * Update one topic in database
	 * 
	 * @param id The topic id
	 * @param topic The topic to be updated
	 */
	void update(Long id, Topic topic);

	/**
	 * Delete one Topic in base of the id
	 * 
	 * @param id The identifier of the Topic to be deleted
	 */
	void delete(Long id);
}
