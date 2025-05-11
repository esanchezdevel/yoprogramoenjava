package com.programandoconjava.infrastructure.payment.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "Paypal", url = "https://api-m.sandbox.paypal.com")
public interface PaypalRequests {

	// TODO Request to implement:
	// curl -v -X POST "https://api-m.sandbox.paypal.com/v1/oauth2/token" \
	// -u "clientId:clientSecret" \
	// -H "Content-Type: application/x-www-form-urlencoded" \
	// -d "grant_type=client_credentials"
	@PostMapping(value = "/v1/oauth2/token", consumes="application/json")
	String authentication();

}
