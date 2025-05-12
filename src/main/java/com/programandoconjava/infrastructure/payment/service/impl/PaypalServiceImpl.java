package com.programandoconjava.infrastructure.payment.service.impl;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.programandoconjava.infrastructure.payment.config.PaymentConfiguration;
import com.programandoconjava.infrastructure.payment.http.dto.Amount;
import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;
import com.programandoconjava.infrastructure.payment.http.dto.Breakdown;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderRequest;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.Item;
import com.programandoconjava.infrastructure.payment.http.dto.PurchaseUnit;
import com.programandoconjava.infrastructure.payment.http.dto.UnitAmount;
import com.programandoconjava.infrastructure.payment.http.request.PaypalRequests;
import com.programandoconjava.infrastructure.payment.service.PaymentService;

@Service
public class PaypalServiceImpl implements PaymentService {

	private static final Logger logger = LogManager.getLogger(PaypalServiceImpl.class);

	public static AuthenticationResponse authenticationToken = null;

	@Autowired
	private PaypalRequests paypalRequest;

	@Autowired
	private PaymentConfiguration paymentConfiguration;

	@Override
	public AuthenticationResponse getAuthToken(boolean useCache) {
		
		if (useCache && authenticationToken != null) {
			logger.debug("Getting authentication token from cache");
			return authenticationToken;
		}

		logger.info("Requesting a new authentication token");
		String clientId = paymentConfiguration.getPaypalClientId();
		String clientSecret = paymentConfiguration.getPaypalClientSecret();

		String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
		String authorizationHeader = "Basic " + credentials;

		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
		formParams.add("grant_type", "client_credentials");

		authenticationToken = paypalRequest.authentication(authorizationHeader, formParams);
		logger.info("Authentication response: {}", authenticationToken);

		return authenticationToken;
	}

	@Override
	public CreateOrderResponse createOrder(String authToken, String productName, String price, String currency) {
		
		String paypalRequestId = UUID.randomUUID().toString();

		String intent = "CAPTURE";

		Item[] items = new Item[1];
		items[0] = new Item(productName, "1", new UnitAmount(currency, price));

		Amount amount = new Amount(currency, price, new Breakdown(new UnitAmount(currency, price)));

		PurchaseUnit[] purchaseUnits = new PurchaseUnit[1];
		purchaseUnits[0] = new PurchaseUnit(items, amount);

		CreateOrderRequest request = new CreateOrderRequest(purchaseUnits, intent);
		
		CreateOrderResponse response = paypalRequest.createOrder("Bearer " + authToken, paypalRequestId, request);

		logger.info("Order created: {}", response);

		return response;
	}
}
