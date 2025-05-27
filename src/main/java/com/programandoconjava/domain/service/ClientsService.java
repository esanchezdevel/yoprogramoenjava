package com.programandoconjava.domain.service;

import java.util.Optional;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.domain.model.Client;

public interface ClientsService {

	/**
	 * Check if a client exists in database.
	 * If exists, return it.
	 * If not exists, store it in the database.
	 * @param name The client name
	 * @param surname The client surname
	 * @param email The client email
	 * @return Optional of the client stored in database.
	 * @throws AppException
	 */
	Optional<Client> store(String name, String surname, String email) throws AppException;

	/**
	 * Get a client looking for the id
	 * @param id The id of the client
	 * @return Optional of the client found
	 * @throws AppException
	 */
	Optional<Client> getById(Long id) throws AppException;
}
