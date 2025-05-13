package com.programandoconjava.presentation.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.programandoconjava.application.utils.Constants;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.infrastructure.payment.config.PaymentConfiguration;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	private static final Logger logger = LogManager.getLogger(PaymentController.class);

	@Autowired
	private ProductsService productsService;

	@Autowired
	private PaymentConfiguration paymentConfiguration;

	@GetMapping()
	public String getPaymentPage(Model model) {

		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		model.addAttribute(Constants.ATTRIBUTE_NAME_PAYPAL_CLIENT_ID, paymentConfiguration.getPaypalClientId());
		
		return "payment-page";
	}

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
