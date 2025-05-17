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

	public static String[] names() {
		String[] values = new String[2];
		int i = 0;
		for (ProductType  productType : ProductType.values()) {
			values[i] = productType.name();
			i++;
		}
		return values;
	}
}
