package com.yoprogramoenjava.domain.service;

import java.util.List;

import com.yoprogramoenjava.domain.model.Topic;

public interface TopicsService {

	/**
	 * Get all the topics from database
	 * 
	 * @return List of Topics
	 */
	public List<Topic> getAll();

	/**
	 * Store one Topic object in database
	 * 
	 * @param topic The topic to be stored
	 */
	public void store(Topic topic);
}
