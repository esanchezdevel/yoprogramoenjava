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

import com.programandoconjava.application.utils.Validators;
import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.domain.service.PurchasesService;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.Item;
import com.programandoconjava.infrastructure.payment.http.dto.PurchaseUnit;
import com.programandoconjava.presentation.dto.mapping.PaymentMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/payment/paypal")
public class PaypalRestController {

	private static final Logger logger = LogManager.getLogger(PaypalRestController.class);

	@Autowired
	private ProductsService productsService;

	@Autowired
	private PurchasesService purchasesService;

	@PostMapping("/create-paypal-order")
	public ResponseEntity<?> createPayPalOrder(@RequestBody Map<String, String> request, HttpServletRequest servletRequest) {

		if (request == null || request.get("product_id") == null || 
			request.get("product_id").length() == 0 || !Validators.isValidLong(request.get("product_id"))) {
			logger.error("Wrong product id received in request. request: {}", request);
			return ResponseEntity.badRequest().build();
		}

		if (request == null || request.get("client_id") == null || 
			request.get("client_id").length() == 0 || !Validators.isValidLong(request.get("client_id"))) {
			logger.error("Wrong client id received in request. request: {}", request);
			return ResponseEntity.badRequest().build();
		}

		Long productId = Long.parseLong(request.get("product_id"));
		Long clientId = Long.parseLong(request.get("client_id"));
		logger.info("Create Order with productId: {}", productId);

		Optional<Product> product = productsService.getById(productId, false);

		if (product.isEmpty()) {
			logger.error("The product with id '{}' not exists in database", productId);
			return ResponseEntity.notFound().build();
		}

		Optional<CreateOrderResponse> order = productsService.createOrder(product.get(), clientId, servletRequest.getRemoteAddr(), servletRequest.getHeader("User-Agent"));

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
			String token = purchasesService.register(productId, orderId, order.get().getPurchaseUnits()[0].customId());

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
