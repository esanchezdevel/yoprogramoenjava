package com.programandoconjava.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programandoconjava.domain.model.Article;
import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.service.HtmlParserService;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.infrastructure.db.dto.ProductDTO;
import com.programandoconjava.infrastructure.db.repository.ProductsRepository;
import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.infrastructure.payment.service.PaymentService;

@Service
public class ProductsServiceImpl implements ProductsService {

	private static final Logger logger = LogManager.getLogger(ProductsServiceImpl.class);

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private HtmlParserService htmlParserService;

	@Autowired
	private ProductsRepository productsRepository;

	@Override
	public Optional<CreateOrderResponse> createOrder(String productName, String price, String currency) {
		logger.info("Creating new order for product={}, price={}, currency={}", productName, price, currency);
		
		CreateOrderResponse order = null;
		try {
			order = executeCreateOrderRequests(true, productName, price, currency);
		} catch (Exception e) {
			if (e.getMessage().contains("401 Unauthorized")) {
				logger.info("Authorization token expired. Requesting new one and retry request");
				try {
					order = executeCreateOrderRequests(false, productName, price, currency);
				} catch (Exception e1) {
					logger.error("Unexpected error happens retrying to create a new order", e1);
					return Optional.empty();
				}
			} else {
				logger.error("Unexpected error happens trying to create a new order", e);
				return Optional.empty();
			}
		}
		return Optional.of(order);
	}

	private CreateOrderResponse executeCreateOrderRequests(boolean getAuthTokenFromCache, String productName, String price, String currency) throws Exception {
		AuthenticationResponse authToken = paymentService.getAuthToken(getAuthTokenFromCache);
		CreateOrderResponse order = paymentService.createOrder(authToken.accessToken(), productName, price, currency);
		logger.info("Order created: {}", order);
		return order;
	}

	@Override
	public Optional<CaptureOrderResponse> captureOrder(String orderId) {
		logger.info("Capturing order {}", orderId);
		
		CaptureOrderResponse order = null;
		try {
			order = executeCaptureOrderRequests(true, orderId);
		} catch (Exception e) {
			if (e.getMessage().contains("401 Unauthorized")) {
				logger.info("Authorization token expired. Requesting new one and retry request");
				try {
					order = executeCaptureOrderRequests(false, orderId);
				} catch (Exception e1) {
					logger.error("Unexpected error happens retrying to capture order", e1);
					return Optional.empty();
				}
			} else {
				logger.error("Unexpected error happens trying to capture order", e);
				return Optional.empty();
			}
		}
		return Optional.of(order);
	}

	private CaptureOrderResponse executeCaptureOrderRequests(boolean getAuthTokenFromCache, String orderId) throws Exception {
		AuthenticationResponse authToken = paymentService.getAuthToken(getAuthTokenFromCache);
		CaptureOrderResponse order = paymentService.captureOrder(authToken.accessToken(), orderId);
		logger.info("Order captured: {}", order);
		return order;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> getAll() {

		List<ProductDTO> productsDTOs = productsRepository.findAllProducts();
		
		List<Product> products = new ArrayList<>();
		productsDTOs.forEach(dto -> {
			Product product = new Product();
			product.setId(dto.id());
			product.setName(dto.name());

			String parsedDescription = htmlParserService.parseToHtml(dto.description());

			product.setDescription(parsedDescription);
			product.setType(dto.type());

			products.add(product);
		});
		return products;
	}

	@Override
	public void store(Product product) {
		productsRepository.save(product);
	}
}
