package com.programandoconjava.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.programandoconjava.domain.service.PurchasesService;
import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.presentation.dto.CaptureOrderResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

	@Mock
	private ProductsService productsService;

	@Mock
	private PurchasesService purchasesService;

	@InjectMocks
	private PaymentController paymentController;

	// Create Order tests
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
		product.setPrice(50.0f);

		CreateOrderResponse createOrderResponse = new CreateOrderResponse("1", "CREATED", new ArrayList<>());

		when(productsService.getById(anyLong(), anyBoolean())).thenReturn(Optional.of(product));
		when(productsService.createOrder(any(Product.class), anyString(), anyString())).thenReturn(Optional.of(createOrderResponse));

		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("User-Agent")).thenReturn("JUnit-Test-Agent");
        when(servletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

		ResponseEntity<?> response = paymentController.createPayPalOrder(request, servletRequest);

		assertNotNull(response);
		assertEquals(201, response.getStatusCode().value());
	}

	@Test
	void whenProductIdNotPresentThenCreateOrderError400() {
		Map<String, String> request = new HashMap<>();

		HttpServletRequest servletRequest = mock(HttpServletRequest.class);

		ResponseEntity<?> response = paymentController.createPayPalOrder(request, servletRequest);
		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());

		request = null;
		response = paymentController.createPayPalOrder(request, servletRequest);
		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());

		request = new HashMap<>();
		request.put("product_id", null);
		response = paymentController.createPayPalOrder(request, servletRequest);
		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());

		request = new HashMap<>();
		request.put("product_id", "");
		response = paymentController.createPayPalOrder(request, servletRequest);
		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());

		request = new HashMap<>();
		request.put("product_id", "test");
		response = paymentController.createPayPalOrder(request, servletRequest);
		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());
	}

	@Test
	void whenProductNotPresentInDatabaseThenCreateOrderError404() {
		Map<String, String> request = new HashMap<>();
		request.put("product_id", "1");

		when(productsService.getById(anyLong(), anyBoolean())).thenReturn(Optional.empty());

		HttpServletRequest servletRequest = mock(HttpServletRequest.class);

		ResponseEntity<?> response = paymentController.createPayPalOrder(request, servletRequest);

		assertNotNull(response);
		assertEquals(404, response.getStatusCode().value());
		verify(productsService, times(0)).createOrder(any(Product.class), anyString(), anyString());
	}

	@Test
	void whenCreateOrderFailsInPaymentPlatformThenError500() {
		Map<String, String> request = new HashMap<>();
		request.put("product_id", "1");

		Product product = new Product();
		product.setId(1L);
		product.setName("test-product");
		product.setDescription("test-description");
		product.setPreviewImage("test-image.jpg");
		product.setPreviewVideo("test-video.mp4");
		product.setPrice(50.0f);

		when(productsService.getById(anyLong(), anyBoolean())).thenReturn(Optional.of(product));
		when(productsService.createOrder(any(Product.class), anyString(), anyString())).thenReturn(Optional.empty());

		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("User-Agent")).thenReturn("JUnit-Test-Agent");
        when(servletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

		ResponseEntity<?> response = paymentController.createPayPalOrder(request, servletRequest);

		assertNotNull(response);
		assertEquals(500, response.getStatusCode().value());
	}

	// Capture Order tests
	@Test
	void whenValidOrderIdReceivedThenCaptureOrderSuccess() {

		Map<String, String> request = new HashMap<>();
		request.put("orderId", "1");
		request.put("product_id", "1");

		String token = "fake-token";

		CaptureOrderResponse captureOrderResponse = new CaptureOrderResponse("", "", "1", null, null, null, "", null, "COMPLETED");

		when(productsService.captureOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(captureOrderResponse));
		when(purchasesService.register(anyString(), anyString())).thenReturn(token);

		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("User-Agent")).thenReturn("JUnit-Test-Agent");
        when(servletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
		
		HttpServletResponse servletResponse = mock(HttpServletResponse.class);

		ResponseEntity<?> response = paymentController.capturePayPalOrder(request, servletRequest, servletResponse);

		assertNotNull(response);
		assertTrue(response.getBody() instanceof CaptureOrderResponseDTO);
		assertEquals(200, response.getStatusCode().value());

		CaptureOrderResponseDTO captureOrderResponseDTO = (CaptureOrderResponseDTO) response.getBody();
		assertEquals("COMPLETED", captureOrderResponseDTO.status());
	}

	@Test
	void whenCaptureOrderFailsInPaymentPlatformThenCaptureOrderError500() {

		Map<String, String> request = new HashMap<>();
		request.put("orderId", "1");
		request.put("product_id", "1");

		when(productsService.captureOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.empty());

		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("User-Agent")).thenReturn("JUnit-Test-Agent");
        when(servletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

		HttpServletResponse servletResponse = mock(HttpServletResponse.class);

		ResponseEntity<?> response = paymentController.capturePayPalOrder(request, servletRequest, servletResponse);

		assertNotNull(response);
		assertEquals(500, response.getStatusCode().value());
	}
}
