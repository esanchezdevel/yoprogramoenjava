package com.programandoconjava.infrastructure.payment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderRequest;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.PurchaseUnit;
import com.programandoconjava.infrastructure.payment.model.Transaction;
import com.programandoconjava.infrastructure.payment.repository.TransactionsRepository;
import com.programandoconjava.infrastructure.payment.service.impl.TransactionsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {

	@Mock
	private TransactionsRepository transactionsRepository;

	@InjectMocks
	private TransactionsServiceImpl transactionsService;

	private String paypalRequestId = "1234";
	private Long productId = 1L;
	private float price = 0.1f;
	private String currency = "EUR";
	private String clientIp = "127.0.0.1";
	private String userAgent = "Test-User-Agent";
	private String payerEmail = "test@test.com";

	@Test
	void whenCreateOrderThenStoreTransaction() throws Exception {

		String operation = "CREATE_ORDER";
		String orderId = "123";
		String status = "CREATED";
		String intent = "CAPTURE";

		PurchaseUnit purchaseUnit = new PurchaseUnit(null, null);
		PurchaseUnit[] purchaseUnits = new PurchaseUnit[1];
		purchaseUnits[0] = purchaseUnit;

		CreateOrderRequest request = new CreateOrderRequest(purchaseUnits, intent);
		CreateOrderResponse response = new CreateOrderResponse(orderId, status, null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonRequest = mapper.writeValueAsString(request);
		String jsonResponse = mapper.writeValueAsString(response);

		Transaction storedTransaction = new Transaction();
		storedTransaction.setPaypalRequestId(paypalRequestId);
		storedTransaction.setClientIp(clientIp);
		storedTransaction.setUserAgent(userAgent);
		storedTransaction.setOperation(operation);
		storedTransaction.setProductId(productId);
		storedTransaction.setAmount(price);
		storedTransaction.setCurrency(currency);
		storedTransaction.setPayerEmail(payerEmail);
		storedTransaction.setStatus(status);
		storedTransaction.setOrderId(orderId);
		storedTransaction.setRequest(jsonRequest);
		storedTransaction.setResponse(jsonResponse);

		when(transactionsRepository.save(any(Transaction.class))).thenReturn(storedTransaction);

		transactionsService.store(paypalRequestId, productId, operation, price, currency, request, response, clientIp, userAgent);

		verify(transactionsRepository, times(1)).save(any(Transaction.class));
	}

	@Test
	void whenCaptureOrderThenStoreTransaction() throws Exception {

		String operation = "CAPTURE_ORDER";
		String orderId = "123";
		String status = "COMPLETED";

		PurchaseUnit purchaseUnit = new PurchaseUnit(null, null);
		PurchaseUnit[] purchaseUnits = new PurchaseUnit[1];
		purchaseUnits[0] = purchaseUnit;

		CaptureOrderResponse response = new CaptureOrderResponse("", "", "", null, null, null, "", null, "");

		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse = mapper.writeValueAsString(response);

		Transaction storedTransaction = new Transaction();
		storedTransaction.setPaypalRequestId(paypalRequestId);
		storedTransaction.setClientIp(clientIp);
		storedTransaction.setUserAgent(userAgent);
		storedTransaction.setOperation(operation);
		storedTransaction.setProductId(productId);
		storedTransaction.setAmount(price);
		storedTransaction.setCurrency(currency);
		storedTransaction.setPayerEmail(payerEmail);
		storedTransaction.setStatus(status);
		storedTransaction.setOrderId(orderId);
		storedTransaction.setResponse(jsonResponse);

		when(transactionsRepository.save(any(Transaction.class))).thenReturn(storedTransaction);

		transactionsService.store(paypalRequestId, productId, operation, price, currency, null, response, clientIp, userAgent);

		verify(transactionsRepository, times(1)).save(any(Transaction.class));
	}
}
