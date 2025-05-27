package com.programandoconjava.domain.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.application.utils.Constants;
import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.model.Purchase;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.domain.service.PurchasesService;
import com.programandoconjava.infrastructure.db.repository.PurchasesRepository;
import com.programandoconjava.infrastructure.payment.http.dto.TransactionOperation;
import com.programandoconjava.infrastructure.payment.model.Transaction;
import com.programandoconjava.infrastructure.payment.service.TransactionsService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PurchasesServiceImpl implements PurchasesService {

	private static final Logger logger = LogManager.getLogger(PurchasesServiceImpl.class);

	private static final String COOKIE_NAME = "product-%d";

	@Autowired
	private ProductsService productsService;

	@Autowired
	private TransactionsService<TransactionOperation> transactionService;

	@Autowired
	private PurchasesRepository purchasesRepository;

	@Override
	public String register(String productId, String orderId) throws AppException {
		
		Optional<Product> product = productsService.getById(Long.parseLong(productId), false);

		if (product.isEmpty()) {
			String errorMsg = String.format("Product '%s' not found in database", productId);
			logger.error(errorMsg);
			throw new AppException(HttpStatus.NOT_FOUND.value(), errorMsg);
		}

		List<Transaction> transactions = transactionService.getTransactionsByOrderId(orderId);

		Optional<Transaction> createOrderTransaction = transactions.stream().filter(t -> "CREATE_ORDER".equals(t.getOperation())).findFirst();
		Optional<Transaction> captureOrderTransaction = transactions.stream().filter(t -> "CAPTURE_ORDER".equals(t.getOperation())).findFirst();

		String clientId = captureOrderTransaction.isPresent()
				? captureOrderTransaction.get().getPayerEmail()
				: Constants.UNKNOWN;

		String token = UUID.randomUUID().toString();

		Purchase purchase = new Purchase();
		purchase.setCreateOrderTransactionId(createOrderTransaction.isPresent()
				? createOrderTransaction.get().getId()
				: null);
		purchase.setCaptureOrderTransactionId(captureOrderTransaction.isPresent()
				? captureOrderTransaction.get().getId()
				: null);
		purchase.setClientId(clientId);
		purchase.setTotalAmount(product.get().getPrice());
		purchase.setNetAmount(calculateNetAmount(product.get()));
		purchase.setTaxAmount(calculateTaxAmount(product.get()));
		purchase.setCurrency(Constants.CURRENCY_EUR);
		purchase.setProduct(product.get());
		purchase.setToken(token);

		purchasesRepository.save(purchase);

		return token;
	}

	private float calculateTaxAmount(Product product) {
		return (product.getTax() * product.getPrice()) / 100;
	}

	private float calculateNetAmount(Product product) {
		return ((100 - product.getTax()) * product.getPrice()) / 100;
	}

	@Override
	public boolean validateDownloadToken(Long productId, HttpServletRequest servletRequest) throws AppException {
		boolean isValid = false;

		Optional<String> cookieValue = getCookie(productId, servletRequest);

		if (cookieValue.isEmpty()) {
			logger.info("Cookie with token not found");
			return isValid;
		}

		Optional<Purchase> purchase = purchasesRepository.findByProductIdAndToken(productId, cookieValue.get());
		if (purchase.isPresent()) {
			logger.info("Purchase found with same productId and Token in database");
			isValid = true;
		}
		return isValid;
	}

	private Optional<String> getCookie(Long productId, HttpServletRequest servletRequest) {
		String cookieName = String.format(COOKIE_NAME, productId);

		logger.info("Looking for cookie: {}", cookieName);

		Optional<String> cookieValue = Optional.empty();
		
		for (Cookie cookie : servletRequest.getCookies()) {
			if (cookieName.equals(cookie.getName())) {
				logger.info("Cookie found");
				cookieValue = Optional.of(cookie.getValue());
				break;
			}
		}
		return cookieValue;
	}
}
