package com.programandoconjava.infrastructure.payment.http.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptureOrderResponse extends TransactionOperation {

	@JsonProperty("create_time") 
	private String createTime;

	@JsonProperty("update_time")
	private String updateTime;

	private String id;

	@JsonProperty("purchase_units") 
	private PurchaseUnit[] purchaseUnits;

	private Link[] links;

	@JsonProperty("payment_source") 
	private PaymentSource paymentSource;

	private String intent;

	private Payer payer;

	private String status;

	public CaptureOrderResponse(String createTime, String updateTime, String id, PurchaseUnit[] purchaseUnits,
			Link[] links, PaymentSource paymentSource, String intent, Payer payer, String status) {
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.id = id;
		this.purchaseUnits = purchaseUnits;
		this.links = links;
		this.paymentSource = paymentSource;
		this.intent = intent;
		this.payer = payer;
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public String getId() {
		return id;
	}

	public PurchaseUnit[] getPurchaseUnits() {
		return purchaseUnits;
	}

	public Link[] getLinks() {
		return links;
	}

	public PaymentSource getPaymentSource() {
		return paymentSource;
	}

	public String getIntent() {
		return intent;
	}

	public Payer getPayer() {
		return payer;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "CaptureOrderResponse [createTime=" + createTime + ", updateTime=" + updateTime + ", id=" + id
				+ ", purchaseUnits=" + Arrays.toString(purchaseUnits) + ", links=" + Arrays.toString(links)
				+ ", paymentSource=" + paymentSource + ", intent=" + intent + ", payer=" + payer + ", status=" + status
				+ "]";
	}
}
