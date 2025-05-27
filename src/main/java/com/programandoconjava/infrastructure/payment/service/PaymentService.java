package com.programandoconjava.infrastructure.payment.service;

import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;

public interface PaymentService {

	/**
	 * Get the authentication token from the payment platform.
	 * This token will be used in all http requests.
	 * 
	 * @param useCache true to get the token previously stored in cache if exists. false to request a new token via api
	 * @return AuthenticationResponse object that contains the token.
	 */
	AuthenticationResponse getAuthToken(boolean useCache);

	CreateOrderResponse createOrder(String authToken, String productId, String productName, String price, String currency, String customClientId, String clientIp, String userAgent);

	CaptureOrderResponse captureOrder(String authToken, String orderId, String productId, String price, String currency, String clientIp, String userAgent);
}
