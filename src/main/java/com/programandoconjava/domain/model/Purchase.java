package com.programandoconjava.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchases")
@EntityListeners(AuditingEntityListener.class)
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@ManyToOne
    @JoinColumn(name = "product_id")
	private Product product;

	private String clientId;

	private float price;

	private String currency;

	@Column(name = "create_order_transaction_id")
	private Long createOrderTransactionId;

	@Column(name = "capture_order_transaction_id")
	private Long captureOrderTransactionId;

	@CreatedDate
	@Column(name = "date_creation")
	private LocalDateTime dateCreation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getCreateOrderTransactionId() {
		return createOrderTransactionId;
	}

	public void setCreateOrderTransactionId(Long createOrderTransactionId) {
		this.createOrderTransactionId = createOrderTransactionId;
	}

	public Long getCaptureOrderTransactionId() {
		return captureOrderTransactionId;
	}

	public void setCaptureOrderTransactionId(Long captureOrderTransactionId) {
		this.captureOrderTransactionId = captureOrderTransactionId;
	}

	public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", token=" + token + ", product=" + product + ", clientId=" + clientId
				+ ", price=" + price + ", currency=" + currency + ", createOrderTransactionId="
				+ createOrderTransactionId + ", captureOrderTransactionId=" + captureOrderTransactionId
				+ ", dateCreation=" + dateCreation + "]";
	}
}
