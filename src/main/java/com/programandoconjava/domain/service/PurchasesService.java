package com.programandoconjava.domain.service;

import com.programandoconjava.application.exception.AppException;

import jakarta.servlet.http.HttpServletRequest;

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

	/**
	 * Validate if the token stored in cookie is valid checking that the 
	 * token is present in purchases table
	 * 
	 * @param productId The id of the Product that will be downloaded
	 * @param servletRequest The http servlet request where we can find the cookies
	 * @return True if it's valid token. False if it's not valid token
	 * @throws AppException
	 */
	boolean validateDownloadToken(Long productId, HttpServletRequest servletRequest) throws AppException;
}
