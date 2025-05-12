package com.programandoconjava.infrastructure.payment.http.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.programandoconjava.infrastructure.payment.http.dto.AuthenticationResponse;

@FeignClient(name = "Paypal", url = "https://api-m.sandbox.paypal.com")
public interface PaypalRequests {

	@PostMapping(value = "/v1/oauth2/token", 
				consumes="application/x-www-form-urlencoded", 
				produces="application/json")
	AuthenticationResponse authentication(@RequestHeader("Authorization") String authorization, 
										@RequestBody MultiValueMap<String, String> formParams);
}
