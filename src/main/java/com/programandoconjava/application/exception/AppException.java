package com.programandoconjava.application.exception;

public class AppException extends RuntimeException {

	private int errorCode;

	private String errorMsg;

	public AppException(int errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "AppException [errorCode=" + errorCode + ", errorMsg=" + errorMsg + "]";
	}
}
