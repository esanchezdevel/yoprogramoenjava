package com.yoprogramoenjava.domain.service;

import java.util.List;

import com.yoprogramoenjava.application.exception.AppException;
import com.yoprogramoenjava.domain.model.ExternalNew;

public interface ExternalNewsService {

	/**
	 * Get all the news from database
	 * 
	 * @return List of all ExternalNew objects found in database
	 * @throws AppException When any error happens
	 */
	List<ExternalNew> getAll() throws AppException;

	/**
	 * Store one ExternalNew object in database
	 * 
	 * @param externalNew The object to be stored
	 * @throws AppException When any error happens
	 */
	void store(ExternalNew externalNew) throws AppException;

	/**
	 * Update one ExternalNew object in database
	 * 
	 * @param id The identifier of the object to be updated
	 * @param externalNew The object with the new data
	 * @throws AppException When any error happens
	 */
	void update(Long id, ExternalNew externalNew) throws AppException;

	/**
	 * Delete one ExternalNew object from database
	 * 
	 * @param id The identifier of the object to be deleted
	 * @throws AppException When any error happens
	 */
	void delete(Long id) throws AppException;
}
