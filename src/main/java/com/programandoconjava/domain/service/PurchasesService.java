package com.programandoconjava.domain.service;

import com.programandoconjava.application.exception.AppException;

public interface PurchasesService {

	/**
	 * Register one new purchase and generates a valid token to allow the download
	 * 
	 * @param productId The id of the product
	 * @param orderId The order id received from payment platform
	 * @return The token generated to validate the purchase
	 * @throws AppException
	 */
	String register(String productId, String orderId) throws AppException;
}
