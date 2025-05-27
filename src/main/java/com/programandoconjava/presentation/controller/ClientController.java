package com.programandoconjava.presentation.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.application.utils.Constants;
import com.programandoconjava.domain.model.Client;
import com.programandoconjava.domain.service.ClientsService;

@Controller
@RequestMapping("/client")
public class ClientController {

	private static final Logger logger = LogManager.getLogger(ClientController.class);

	@Autowired
	private ClientsService clientsService;

	@PostMapping("/create")
	public String storeClient(@RequestBody Map<String, String> request, Model model) {
		if (request == null || request.isEmpty()) {
			String errorMsg = "No input parameters received";
			logger.error(errorMsg);
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}
		if (request.get("productId") == null || request.get("productId").isEmpty()) {
			String errorMsg = "Empty mandatory parameter 'productId'";
			logger.error(errorMsg);
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}
		if (request.get("name") == null || request.get("name").isEmpty()) {
			String errorMsg = "Empty mandatory parameter 'name'";
			logger.error(errorMsg);
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}
		if (request.get("surname") == null || request.get("surname").isEmpty()) {
			String errorMsg = "Empty mandatory parameter 'surname'";
			logger.error(errorMsg);
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}
		if (request.get("email") == null || request.get("email").isEmpty()) {
			String errorMsg = "Empty mandatory parameter 'email'";
			logger.error(errorMsg);
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}

		String productId = request.get("productId");
		String name = request.get("name");
		String surname = request.get("surname");
		String email = request.get("email");

		try {
			Optional<Client> client = clientsService.store(name, surname, email);
			
			if (client.isEmpty()) {
				throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error happened. Client not stored in database");	
			}
			return "redirect:/payment/" + productId + "/" + client.get().getId();
		} catch (AppException e) {
			String errorMsg = "Error storing client in database: " + e.getMessage();
			logger.error(errorMsg, e);
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}
	}
}
