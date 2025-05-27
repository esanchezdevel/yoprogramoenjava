package com.programandoconjava.presentation.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.domain.model.Client;
import com.programandoconjava.domain.service.ClientsService;
import com.programandoconjava.presentation.dto.ClientDTO;

@Controller
@RequestMapping("/client")
public class ClientController {

	private static final Logger logger = LogManager.getLogger(ClientController.class);

	@Autowired
	private ClientsService clientsService;

	@PostMapping("/create")
	public ResponseEntity<?> storeClient(@RequestBody Map<String, String> request) {
		if (request == null || request.isEmpty()) {
			logger.error("No input parameters received");
			return ResponseEntity.badRequest().build();
		}
		if (request.get("name") == null || request.get("name").isEmpty()) {
			logger.error("Empty mandatory parameter 'name'");
			return ResponseEntity.badRequest().build();
		}
		if (request.get("surname") == null || request.get("surname").isEmpty()) {
			logger.error("Empty mandatory parameter 'surname'");
			return ResponseEntity.badRequest().build();
		}
		if (request.get("email") == null || request.get("email").isEmpty()) {
			logger.error("Empty mandatory parameter 'email'");
			return ResponseEntity.badRequest().build();
		}

		String name = request.get("name");
		String surname = request.get("surname");
		String email = request.get("email");

		try {
			Optional<Client> client = clientsService.store(name, surname, email);
			
			if (client.isEmpty()) {
				throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error happened. Client not stored in database");	
			}
			return ResponseEntity.ok().body(new ClientDTO(String.valueOf(client.get().getId())));
		} catch (AppException e) {
			logger.error("Error storing client: name={}, surname={}, email={}", name, surname, email, e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
