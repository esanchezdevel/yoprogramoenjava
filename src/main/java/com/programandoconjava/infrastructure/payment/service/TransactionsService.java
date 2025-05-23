package com.programandoconjava.infrastructure.payment.service;

import java.util.List;

import com.programandoconjava.infrastructure.payment.http.dto.TransactionOperation;
import com.programandoconjava.infrastructure.payment.model.Transaction;

public interface TransactionsService<T extends TransactionOperation> {

	/**
	 * Store in transactions db table one payment transaction
	 * 
	 * @param paypalRequestId The unique ID sent in headers to identify the request in paypal
	 * @param productId The id of the product purchased
	 * @param operation The operation of the transaction
	 * @param price The price of the product
	 * @param currency The currency of the price
	 * @param request The request of the transaction
	 * @param response The response of the transaction
	 * @param clientIp The IP of the user
	 * @param userAgent The User Agent of the user
	 */
	void store(String paypalRequestId, Long productId, String operation, float price, String currency, T request, T response, String clientIp, String userAgent);

	/**
	 * Get the transactions that have the same orderId
	 * 
	 * @param orderId The order id to look for
	 * @return The list that contains the Transactions found
	 */
	List<Transaction> getTransactionsByOrderId(String orderId);
}
