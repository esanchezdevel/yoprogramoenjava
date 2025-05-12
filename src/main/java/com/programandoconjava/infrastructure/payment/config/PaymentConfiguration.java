package com.programandoconjava.infrastructure.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfiguration {
	
	@Value("${payments.paypal.client-id}")
	private String paypalClientId;

	@Value("${payments.paypal.client-secret}")
	private String paypalClientSecret;

	public String getPaypalClientId() {
		return paypalClientId;
	}

	public String getPaypalClientSecret() {
		return paypalClientSecret;
	}
}
