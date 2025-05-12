package com.programandoconjava.infrastructure.payment.service;

import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;

public interface PaymentService {

	/**
	 * Get the authentication token from the payment platform.
	 * This token will be used in all http requests.
	 * 
	 * @param useCache true to get the token previously stored in cache if exists. false to request a new token via api
	 * @return AuthenticationResponse object that contains the token.
	 */
	AuthenticationResponse getAuthToken(boolean useCache);
}
