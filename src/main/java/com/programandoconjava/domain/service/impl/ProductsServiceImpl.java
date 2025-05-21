package com.programandoconjava.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programandoconjava.application.utils.Constants;
import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.model.ProductType;
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
	public Optional<CreateOrderResponse> createOrder(Product product, String clientIp, String userAgent) {
		logger.info("Creating new order for product={}", product.getId());
		
		CreateOrderResponse order = null;
		try {
			order = executeCreateOrderRequests(true, product, clientIp, userAgent);
		} catch (Exception e) {
			if (e.getMessage().contains("401 Unauthorized")) {
				logger.info("Authorization token expired. Requesting new one and retry request");
				try {
					order = executeCreateOrderRequests(false, product, clientIp, userAgent);
				} catch (Exception e1) {
					logger.error("Unexpected error happens retrying to create a new order", e1);
					return Optional.empty();
				}
			} else {
				logger.error("Unexpected error happens trying to create a new order", e);
				return Optional.empty();
			}
		}

		if (!"CREATED".equals(order.getStatus())) {
			logger.error("Unexpected status received in Create Order response: '{}'", order.getStatus());
			return Optional.empty();
		}

		return Optional.of(order);
	}

	private CreateOrderResponse executeCreateOrderRequests(boolean getAuthTokenFromCache, Product product, String clientIp, String userAgent) throws Exception {
		AuthenticationResponse authToken = paymentService.getAuthToken(getAuthTokenFromCache);
		CreateOrderResponse order = paymentService.createOrder(authToken.accessToken(), String.valueOf(product.getId()), product.getName(), String.valueOf(product.getPrice()), Constants.CURRENCY_EUR, clientIp, userAgent);
		logger.info("Order created: {}", order);
		return order;
	}

	@Override
	public Optional<CaptureOrderResponse> captureOrder(String orderId, String productId, String clientIp, String userAgent) {
		logger.info("Capturing order {}", orderId);
		
		Optional<Product> product = getById(Long.parseLong(productId), false);

		if (product.isEmpty()) {
			logger.error("Product id '{}' not found in database");
			return Optional.empty();
		}

		CaptureOrderResponse order = null;
		try {
			order = executeCaptureOrderRequests(true, orderId, product.get(), clientIp, userAgent);
		} catch (Exception e) {
			if (e.getMessage().contains("401 Unauthorized")) {
				logger.info("Authorization token expired. Requesting new one and retry request");
				try {
					order = executeCaptureOrderRequests(false, orderId, product.get(), clientIp, userAgent);
				} catch (Exception e1) {
					logger.error("Unexpected error happens retrying to capture order", e1);
					return Optional.empty();
				}
			} else {
				logger.error("Unexpected error happens trying to capture order", e);
				return Optional.empty();
			}
		}

		if (!"COMPLETED".equals(order.getStatus())) {
			logger.error("Unexpected status received in Capture Order response: '{}'", order.getStatus());
			return Optional.empty();
		}

		return Optional.of(order);
	}

	private CaptureOrderResponse executeCaptureOrderRequests(boolean getAuthTokenFromCache, String orderId, Product product, String clientIp, String userAgent) throws Exception {
		AuthenticationResponse authToken = paymentService.getAuthToken(getAuthTokenFromCache);
		CaptureOrderResponse order = paymentService.captureOrder(authToken.accessToken(), orderId, String.valueOf(product.getId()), String.valueOf(product.getPrice()), Constants.CURRENCY_EUR, clientIp, userAgent);
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
	public Optional<Product> getById(long id, boolean parseToHtml) {
		Optional<Product> product = productsRepository.findById(id);

		if (product.isEmpty()) {
			logger.warn("Product with id '{}' not found", id);
			return product;
		}

		String parsedDescription = parseToHtml ? htmlParserService.parseToHtml(product.get().getDescription()) : product.get().getDescription();

		product.get().setDescription(parsedDescription);

		return product;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> getByType(ProductType type, boolean parseToHtml) {
		List<Product> products = productsRepository.findByType(type);

		if (parseToHtml) {
			products.forEach(p -> p.setDescription(htmlParserService.parseToHtml(p.getDescription())));
		}
		return products;
	}

	@Override
	public void store(Product product) {
		productsRepository.save(product);
	}

	@Override
	@Transactional
	public void update(Long id, Product product) {
		
		Optional<Product> productDb = productsRepository.findById(id);

		if (productDb.isPresent()) {
			productDb.get().setName(product.getName());
			productDb.get().setDescription(product.getDescription());
			productDb.get().setType(product.getType());
			productDb.get().setPreviewImage(product.getPreviewImage());
			productDb.get().setPreviewVideo(product.getPreviewVideo());
			productDb.get().setPrice(product.getPrice());
		}
	}

	@Override
	public void delete(Long id) {
		productsRepository.deleteById(id);
	}
}
