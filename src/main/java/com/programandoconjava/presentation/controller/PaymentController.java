package com.programandoconjava.presentation.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.application.utils.Validators;
import com.programandoconjava.domain.model.Client;
import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.service.ClientsService;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.domain.service.PurchasesService;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.presentation.dto.ClientDTO;
import com.programandoconjava.presentation.dto.mapping.PaymentMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	private static final Logger logger = LogManager.getLogger(PaymentController.class);

	@Autowired
	private ProductsService productsService;

	@Autowired
	private PurchasesService purchasesService;

	@Autowired
	private ClientsService clientsService;

	@PostMapping("/client")
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

	@PostMapping("/create-paypal-order")
	public ResponseEntity<?> createPayPalOrder(@RequestBody Map<String, String> request, HttpServletRequest servletRequest) {

		if (request == null || request.get("product_id") == null || 
			request.get("product_id").length() == 0 || !Validators.isValidLong(request.get("product_id"))) {
			logger.error("Wrong product id received in request. request: {}", request);
			return ResponseEntity.badRequest().build();
		}

		Long productId = Long.parseLong(request.get("product_id"));
		logger.info("Create Order with productId: {}", productId);

		Optional<Product> product = productsService.getById(productId, false);

		if (product.isEmpty()) {
			logger.error("The product with id '{}' not exists in database", productId);
			return ResponseEntity.notFound().build();
		}

		Optional<CreateOrderResponse> order = productsService.createOrder(product.get(), servletRequest.getRemoteAddr(), servletRequest.getHeader("User-Agent"));

		if (order.isEmpty()) {
			logger.error("The process to create a new order failed");
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(PaymentMapping.parseCreateOrderResponseToDTO(order.get()));
	}

	@PostMapping("/capture-paypal-order")
	public ResponseEntity<?> capturePayPalOrder(@RequestBody Map<String, String> request,
			HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		String orderId = request.get("orderId");
		String productId = request.get("product_id");

		Optional<CaptureOrderResponse> order = productsService.captureOrder(orderId, productId, servletRequest.getRemoteAddr(), servletRequest.getHeader("User-Agent"));

		if (order.isEmpty()) {
			logger.error("The process to capture a new order failed");
			return ResponseEntity.internalServerError().build();
		}
		if ("COMPLETED".equals(order.get().getStatus())) {
			logger.debug("Purchase completed. register it in database and set cookie to validate the product download");
			String token = purchasesService.register(productId, orderId);

			Cookie cookie = new Cookie("product-" + productId, token);
			cookie.setHttpOnly(true); 								// Prevent access via JavaScript
			cookie.setSecure(true);									// Send only over HTTPS
			cookie.setPath("/products/download");						// Path where cookie is valid
			cookie.setMaxAge(1 * 24 * 60 * 60);								// 1 day
	
			servletResponse.addCookie(cookie);
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(PaymentMapping.parseCaptureOrderResponseToDTO(order.get()));
	}
}
