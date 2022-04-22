package com.test.demo.exception;

public class AccountNumberNotPresentException extends CustomException {
	public String getMessage() {
		return "Please enter the account number and try again.";
	}
}
