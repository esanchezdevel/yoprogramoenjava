package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CaptureOrderResponse(@JsonProperty("create_time") String createTime,
								@JsonProperty("update_time") String updateTime,
								String id,
								@JsonProperty("purchase_units") PurchaseUnit[] purchaseUnits,
								Link[] links,
								@JsonProperty("payment_source") PaymentSource paymentSource,
								String intent,
								Payer payer,
								String status) {

}
