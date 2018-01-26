package com.eulen.api.formatter;

public class ErrorMessage {

	private final String errorMessage;
	
	public ErrorMessage(String message) {
		this.errorMessage = message;
	}

	public String getMessage() {
		return errorMessage;
	}
}
