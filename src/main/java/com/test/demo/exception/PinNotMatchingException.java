package com.test.demo.exception;

public class PinNotMatchingException extends CustomException {
	public String getMessage() {
		return "Invalid PIN. Please try again.";
	}
}
