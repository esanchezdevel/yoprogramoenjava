package com.programandoconjava.domain.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.infrastructure.payment.service.PaymentService;

@Service
public class ProductsServiceImpl implements ProductsService {

	private static final Logger logger = LogManager.getLogger(ProductsServiceImpl.class);

	@Autowired
	private PaymentService paymentService;

	@Override
	public Optional<CreateOrderResponse> createOrder(String productName, String price, String currency) {
		logger.info("Creating new order for product={}, price={}, currency={}", productName, price, currency);
		
		CreateOrderResponse order = null;
		try {
			order = executeCreateOrderRequests(true, productName, price, currency);
		} catch (Exception e) {
			if (e.getMessage().contains("401 Unauthorized")) {
				logger.info("Authorization token expired. Requesting new one and retry request");
				try {
					order = executeCreateOrderRequests(false, productName, price, currency);
				} catch (Exception e1) {
					logger.error("Unexpected error happens retrying to create a new order", e1);
					return Optional.empty();
				}
			} else {
				logger.error("Unexpected error happens trying to create a new order", e);
				return Optional.empty();
			}
		}
		return Optional.of(order);
	}

	private CreateOrderResponse executeCreateOrderRequests(boolean getAuthTokenFromCache, String productName, String price, String currency) throws Exception {
		AuthenticationResponse authToken = paymentService.getAuthToken(getAuthTokenFromCache);
		CreateOrderResponse order = paymentService.createOrder(authToken.accessToken(), productName, price, currency);
		logger.info("Order created: {}", order);
		return order;
	}
}
