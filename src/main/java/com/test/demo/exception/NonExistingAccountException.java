package com.test.demo.exception;

public class NonExistingAccountException extends CustomException {
	public String getMessage() {
		return "Account does not exist. Please try again.";
	}
}
