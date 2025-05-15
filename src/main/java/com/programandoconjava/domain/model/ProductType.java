package com.programandoconjava.domain.model;

public enum ProductType {

	WEB_TEMPLATE("web_template"),
	MICROSERVICE("microservice");

	private String value;
	
	private ProductType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
