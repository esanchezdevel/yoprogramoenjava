package com.programandoconjava.infrastructure.payment.http.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderResponse extends TransactionOperation {

	private String id;
	
	private String status;
	
	private List<Link> links;

	public CreateOrderResponse(String id, String status, List<Link> links) {
		this.id = id;
		this.status = status;
		this.links = links;
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public List<Link> getLinks() {
		return links;
	}

	@Override
	public String toString() {
		return "CreateOrderResponse [id=" + id + ", status=" + status + ", links=" + links + "]";
	}
}
