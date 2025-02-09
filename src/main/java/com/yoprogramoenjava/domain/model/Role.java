package com.yoprogramoenjava.domain.model;

public enum Role {

	ADMIN("admin"),
	USER("user");

	private String value;
	
	private Role(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
