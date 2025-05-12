package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateOrderRequest(@JsonProperty("purchase_units") PurchaseUnit[] purchaseUnits, String intent) {

}
