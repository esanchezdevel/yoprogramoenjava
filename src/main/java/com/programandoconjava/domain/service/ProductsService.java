package com.programandoconjava.domain.service;

public interface ProductsService {

	/**
	 * Create an order into the Payment platform
	 * 
	 * @param productName The name of the product to be purchased
	 * @param price The price of the product
	 * @param currency The currency of the price applied
	 */
	void createOrder(String productName, String price, String currency);
}
