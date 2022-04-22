package com.test.demo.exception;

public class NotEnoughBalanceException extends CustomException {
	Double withdrawableAmount;
	Integer withdrawalAmount;
	
	public NotEnoughBalanceException(Integer withdrawalAmount, Double withdrawableAmount) {
		this.withdrawalAmount=withdrawalAmount;
		this.withdrawableAmount=withdrawableAmount;
	}
	
	public NotEnoughBalanceException() {
		
	}
	public String getMessage() {
		if(withdrawableAmount!=null && withdrawalAmount!=null)
			return "You do not have enough balance in the account. (Total WithdrawableAmount amount: "+withdrawableAmount+", Entered amount: "+withdrawalAmount+".) Please try a different amount";
		else
			return "You do not have enough balance in the account. Please try a different amount";
	}
}
