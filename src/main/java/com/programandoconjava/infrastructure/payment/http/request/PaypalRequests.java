package com.programandoconjava.infrastructure.payment.http.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderRequest;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;

@FeignClient(name = "Paypal", url = "https://api-m.sandbox.paypal.com")
public interface PaypalRequests {

	@PostMapping(value = "/v1/oauth2/token", 
				consumes="application/x-www-form-urlencoded", 
				produces="application/json")
	AuthenticationResponse authentication(@RequestHeader("Authorization") String authorization, 
										@RequestBody MultiValueMap<String, String> formParams);

	@PostMapping(value = "/v2/checkout/orders",
				consumes = "application/json",
				produces = "application/json")
	CreateOrderResponse createOrder(@RequestHeader("Authorization") String authorization, 
									@RequestHeader("PayPal-Request-Id") String paypalRequestId,
									@RequestBody CreateOrderRequest request);
}
