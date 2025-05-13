package com.programandoconjava.domain.service;

import java.util.Optional;

import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;

public interface ProductsService {

	/**
	 * Create an order into the Payment platform
	 * 
	 * @param productName The name of the product to be purchased
	 * @param price The price of the product
	 * @param currency The currency of the price applied
	 * @return Optional of CreateOrderResponse that contains the id of the order created. Empty if the process failed
	 */
	Optional<CreateOrderResponse> createOrder(String productName, String price, String currency);

	/**
	 * Capture an order to complete the transaction in the payment platform
	 * 
	 * @param orderId The id of the order to be captured
	 * @return Optional of CaptureOrderResponse that contains the data related to the order. Empty if the process failed
	 */
	Optional<CaptureOrderResponse> captureOrder(String orderId);
}
