package com.programandoconjava.infrastructure.payment.service;

import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;
import com.programandoconjava.infrastructure.payment.http.request.PaypalRequests;

@Service
public class PaypalServiceImpl implements PaymentService {

	private static final Logger logger = LogManager.getLogger(PaypalServiceImpl.class);

	public static AuthenticationResponse authenticationToken = null;

	@Autowired
	private PaypalRequests paypalRequest;

	@Override
	public AuthenticationResponse getAuthToken(boolean useCache) {
		
		if (useCache && authenticationToken != null) {
			logger.debug("Getting authentication token from cache");
			return authenticationToken;
		}

		logger.debug("Requesting a new authentication token");
		String clientId = "";
		String clientSecret = "";
		String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
		String authorizationHeader = "Basic " + credentials;

		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
		formParams.add("grant_type", "client_credentials");

		authenticationToken = paypalRequest.authentication(authorizationHeader, formParams);
		logger.debug("Authentication response: {}", authenticationToken);

		return authenticationToken;
	}
}
