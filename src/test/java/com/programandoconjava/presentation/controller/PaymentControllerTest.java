package com.programandoconjava.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

	@Mock
	private ProductsService productsService;

	@InjectMocks
	private PaymentController paymentController;

	@Test
	void whenValidIdReceivedThenCreateOrderSuccess() {

		Map<String, String> request = new HashMap<>();
		request.put("product_id", "1");

		Product product = new Product();
		product.setId(1L);
		product.setName("test-product");
		product.setDescription("test-description");
		product.setPreviewImage("test-image.jpg");
		product.setPreviewVideo("test-video.mp4");
		product.setPrice(50.0);

		CreateOrderResponse createOrderResponse = new CreateOrderResponse("1", "CREATED", new ArrayList<>());

		when(productsService.getById(anyLong(), anyBoolean())).thenReturn(Optional.of(product));
		when(productsService.createOrder(anyString(), anyString(), anyString())).thenReturn(Optional.of(createOrderResponse));

		ResponseEntity<?> response = paymentController.createPayPalOrder(request);

		assertNotNull(response);
		assertEquals(201, response.getStatusCode().value());
	}

	@Test
	void whenProductIdNotPresentThenCreateOrderError400() {
		Map<String, String> request = new HashMap<>();

		ResponseEntity<?> response = paymentController.createPayPalOrder(request);
		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());
	}
}
