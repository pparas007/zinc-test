package com.test.demo.exception;

public class PinNotEnteredException extends CustomException {
	public String getMessage() {
		return "Please enter the pin and try again.";
	}
}
