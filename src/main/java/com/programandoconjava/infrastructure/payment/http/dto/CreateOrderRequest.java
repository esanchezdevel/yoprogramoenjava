package com.programandoconjava.infrastructure.payment.http.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrderRequest extends TransactionOperation {

	@JsonProperty("purchase_units")
	private PurchaseUnit[] purchaseUnits;
	
	private String intent;

	public CreateOrderRequest(PurchaseUnit[] purchaseUnits, String intent) {
		this.purchaseUnits = purchaseUnits;
		this.intent = intent;
	}

	public PurchaseUnit[] getPurchaseUnits() {
		return purchaseUnits;
	}

	public String getIntent() {
		return intent;
	}

	@Override
	public String toString() {
		return "CreateOrderRequest [purchaseUnits=" + Arrays.toString(purchaseUnits) + ", intent=" + intent + "]";
	}
}
