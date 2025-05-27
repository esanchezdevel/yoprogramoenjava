package com.programandoconjava.domain.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.domain.model.Client;
import com.programandoconjava.domain.service.ClientsService;
import com.programandoconjava.infrastructure.db.repository.ClientsRepository;

@Service
public class ClientsServiceImpl implements ClientsService {

	private static final Logger logger = LogManager.getLogger(ClientsService.class);

	@Autowired
	private ClientsRepository clientsRepository;

	@Override
	public Optional<Client> store(String name, String surname, String email) throws AppException {
		
		try {
			Optional<Client> client = clientsRepository.findByEmail(email);
			if (client.isPresent()) {
				logger.info("Client already present with email '{}'", email);
				return client;
			}
		} catch (Exception e) {
			String errorMsg = "Error checking if client is present in database: " + e.getMessage();
			logger.error(errorMsg);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}

		Client c = new Client();
		c.setName(name);
		c.setSurname(surname);
		c.setEmail(email);

		try {
			Client savedClient = clientsRepository.save(c);
			return Optional.of(savedClient);
		} catch (Exception e) {
			String errorMsg = "Error storing client into database: " + e.getMessage();
			logger.error(errorMsg);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}
}
