package com.programandoconjava.domain.model;

public enum Currency {

	EUR("EUR"),
	USD("USD"),
	GBP("GBP");

	private String value;

	private Currency(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
