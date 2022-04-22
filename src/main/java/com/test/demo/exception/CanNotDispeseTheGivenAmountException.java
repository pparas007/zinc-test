package com.test.demo.exception;

public class CanNotDispeseTheGivenAmountException extends CustomException {
	public String getMessage() {
		return "Sorry, we can not dispense the entered amount. Please try a different amount.";
	}
}