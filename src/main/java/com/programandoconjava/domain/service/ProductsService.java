package com.programandoconjava.domain.service;

import java.util.List;
import java.util.Optional;

import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.model.ProductType;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;

public interface ProductsService {

	/**
	 * Create an order into the Payment platform
	 * 
	 * @param product The product to be purchased
	 * @param clientId The client id stored in our database
	 * @param clientIp The IP of the client
	 * @param userAgent The User Agent of the client
	 * @return Optional of CreateOrderResponse that contains the id of the order created. Empty if the process failed
	 */
	Optional<CreateOrderResponse> createOrder(Product product, Long clientId, String clientIp, String userAgent);

	/**
	 * Capture an order to complete the transaction in the payment platform
	 * 
	 * @param orderId The id of the order to be captured
	 * @param productId The id of the product that is being purchased
	 * @param clientIp The IP of the client
	 * @param userAgent The request header user agent of the user
	 * @return Optional of CaptureOrderResponse that contains the data related to the order. Empty if the process failed
	 */
	Optional<CaptureOrderResponse> captureOrder(String orderId, String productId, String clientIp, String userAgent);

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
	 * Get all Products looking by the type
	 * 
	 * @param type The type of the Product
	 * @param parseToHtml True to indicate if we want to parse the description of the product to HTML format
	 * @return List of Products found
	 */
	List<Product> getByType(ProductType type, boolean parseToHtml);

	/**
	 * Store one Product in database
	 * 
	 * @param product The product to be saved
	 */
	void store(Product product);

	/**
	 * Update one product in database
	 * 
	 * @param id The product id
	 * @param article The product to be updated
	 */
	void update(Long id, Product product);

	/**
	 * Delete one Product in base of the id
	 * 
	 * @param id The identifier of the Product to be deleted
	 */
	void delete(Long id);
}
