package com.programandoconjava.presentation.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	private static final Logger logger = LogManager.getLogger(PaymentController.class);

	@Autowired
	private ProductsService productsService;

	@PostMapping("/create-paypal-order")
	public ResponseEntity<?> createPayPalOrder(Model model, @RequestBody Map<String, String> request) {

		if (request == null || request.get("product_id") == null || 
			request.get("product_id").length() == 0 || !isValidLong(request.get("product_id"))) {
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

		Optional<CreateOrderResponse> order = productsService.createOrder(product.get().getName(), String.valueOf(product.get().getPrice()), "EUR");

		if (order.isEmpty()) {
			logger.error("The process to create a new order failed");
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(order);
	}

	private boolean isValidLong(String value) {
		try {
			Long.valueOf(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@PostMapping("/capture-paypal-order")
	public ResponseEntity<?> capturePayPalOrder(@RequestBody Map<String, String> body) {
		String orderId = body.get("orderId");

		Optional<CaptureOrderResponse> order = productsService.captureOrder(orderId);

		if (order.isEmpty()) {
			logger.error("The process to capture a new order failed");
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(order);
	}
}
