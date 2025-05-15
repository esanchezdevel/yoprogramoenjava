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
	public ResponseEntity<?> createPayPalOrder(Model model) {

		Optional<CreateOrderResponse> order = productsService.createOrder("template-001", "0.1", "EUR");

		if (order.isEmpty()) {
			logger.error("The process to create a new order failed");
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(order);
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
