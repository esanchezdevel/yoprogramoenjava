package com.programandoconjava.infrastructure.payment.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions", indexes = {
	@Index(name = "idx_transaction_operation", columnList = "operation"),
	@Index(name = "idx_transaction_status", columnList = "status"),
	@Index(name = "idx_transaction_product", columnList = "product_id"),
})
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "paypal_request_id")
	private String paypalRequestId;

	private String operation;

	private String status;

	private float amount;

	private String currency;

	@Column(name = "payer_email")
	private String payerEmail;

	@Column(name = "product_id")
	private Long productId;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String request;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String response;

	@Column(name = "client_ip")
	private String clientIp;

	@Lob
	@Column(name = "user_agent", columnDefinition = "TEXT")
	private String userAgent;

	@CreatedDate
	@Column(name = "date_creation")
	private LocalDateTime dateCreation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaypalRequestId() {
		return paypalRequestId;
	}

	public void setPaypalRequestId(String paypalRequestId) {
		this.paypalRequestId = paypalRequestId;
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", orderId=" + orderId + ", paypalRequestId=" + paypalRequestId
				+ ", operation=" + operation + ", status=" + status + ", amount=" + amount + ", currency=" + currency
				+ ", payerEmail=" + payerEmail + ", productId=" + productId + ", request=" + request + ", response="
				+ response + ", clientIp=" + clientIp + ", userAgent=" + userAgent + ", dateCreation=" + dateCreation
				+ "]";
	}
}
