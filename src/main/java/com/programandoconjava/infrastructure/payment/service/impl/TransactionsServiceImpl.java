package com.programandoconjava.infrastructure.payment.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderRequest;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.TransactionOperation;
import com.programandoconjava.infrastructure.payment.model.Transaction;
import com.programandoconjava.infrastructure.payment.repository.TransactionsRepository;
import com.programandoconjava.infrastructure.payment.service.TransactionsService;

@Service
public class TransactionsServiceImpl implements TransactionsService<TransactionOperation> {

	private static final Logger logger = LogManager.getLogger(TransactionsServiceImpl.class);

	private static final String EMPTY = "";
	private static final String UNKNOWN = "unknown";

	@Autowired
	private TransactionsRepository transactionsRepository;

	@Override
	public void store(String paypalRequestId, Long productId, String operation, float price, String currency, TransactionOperation request, TransactionOperation response, String clientIp,
			String userAgent) {

		String orderId = null;
		String status = null;
		String payerEmail = null;
		String jsonRequest = null;
		String jsonResponse = null;

		ObjectMapper mapper = new ObjectMapper();

		if (request instanceof CreateOrderRequest) {
			CreateOrderRequest createOrderRequest = (CreateOrderRequest) request;
			try {
				jsonRequest = mapper.writeValueAsString(createOrderRequest);
			} catch (JsonProcessingException e) {
				logger.error("Error parsing create order request to json string", e);
				jsonRequest = EMPTY;
			}
		}

		if (response instanceof CreateOrderResponse) {
			CreateOrderResponse createOrderResponse = (CreateOrderResponse) response;
			orderId = createOrderResponse.getId();
			status = createOrderResponse.getStatus();
			payerEmail = EMPTY;
			try {
				jsonResponse = mapper.writeValueAsString(createOrderResponse);
			} catch (JsonProcessingException e) {
				logger.error("Error parsing create order response to json string", e);
				jsonResponse = EMPTY;
			}
		} else if (response instanceof CaptureOrderResponse) {
			CaptureOrderResponse captureOrderResponse = (CaptureOrderResponse) response;
			orderId = captureOrderResponse.getId();
			status = captureOrderResponse.getStatus();
			payerEmail = captureOrderResponse.getPayer() != null
					&& captureOrderResponse.getPayer().emailAddress() != null
							? captureOrderResponse.getPayer().emailAddress()
							: UNKNOWN;
			try {
				jsonResponse = mapper.writeValueAsString(captureOrderResponse);
			} catch (JsonProcessingException e) {
				logger.error("Error parsing capture order response to json string", e);
				jsonResponse = EMPTY;
			}
		}

		Transaction transaction = new Transaction();
		transaction.setPaypalRequestId(paypalRequestId);
		transaction.setClientIp(clientIp);
		transaction.setUserAgent(userAgent);
		transaction.setOperation(operation);
		transaction.setProductId(productId);
		transaction.setAmount(price);
		transaction.setCurrency(currency);
		transaction.setPayerEmail(payerEmail);
		transaction.setStatus(status);
		transaction.setOrderId(orderId);
		transaction.setRequest(jsonRequest);
		transaction.setResponse(jsonResponse);

		transactionsRepository.save(transaction);
	}

	@Override
	public List<Transaction> getTransactionsByOrderId(String orderId) {
		List<Transaction> transactions = transactionsRepository.findAllByOrderId(orderId);

		logger.debug("Found '{}' transactions with orderId '{}'", transactions != null ? String.valueOf(transactions.size()) : "0", orderId);
		return transactions;
	}
}
