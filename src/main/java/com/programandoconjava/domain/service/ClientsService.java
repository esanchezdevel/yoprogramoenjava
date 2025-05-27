package com.programandoconjava.domain.service;

import java.util.Optional;

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
	 */
	Optional<Client> store(String name, String surname, String email);
}
