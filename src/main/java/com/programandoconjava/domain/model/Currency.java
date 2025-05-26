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

	public static String[] names() {
		String[] values = new String[3];
		int i = 0;
		for (Currency  currency : Currency.values()) {
			values[i] = currency.name();
			i++;
		}
		return values;
	}
}
