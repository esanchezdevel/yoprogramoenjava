package com.programandoconjava.application.utils;

public class Validators {

	public static boolean isValidLong(String value) {
		try {
			Long.valueOf(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
