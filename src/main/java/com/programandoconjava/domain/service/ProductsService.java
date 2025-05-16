package com.programandoconjava.domain.service;

import java.util.List;
import java.util.Optional;

import com.programandoconjava.domain.model.Product;
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

	/**
	 * Get all the Products stored in database
	 * 
	 * @return List of Products
	 */
	List<Product> getAll();

	/**
	 * Get one Product looking by the Id
	 * 
	 * @param id The id of the Product
	 * @param parseToHtml True to indicate if we want to parse the content of the product to HTML format
	 * @return Optional of Product. Empty if the Product is not found
	 */
	Optional<Product> getById(long id, boolean parseToHtml);

	/**
	 * Store one Product in database
	 * 
	 * @param product The product to be saved
	 */
	void store(Product product);
}
