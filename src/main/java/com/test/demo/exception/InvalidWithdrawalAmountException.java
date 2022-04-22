package com.test.demo.exception;

public class InvalidWithdrawalAmountException extends CustomException {
	public String getMessage() {
		return "Please enter a valid withdrawal amount & try again.";
	}
}