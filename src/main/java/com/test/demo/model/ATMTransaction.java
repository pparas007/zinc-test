package com.test.demo.model;

import java.util.Map;

public class ATMTransaction {
	private Account account;
	private Integer withdrawAmount;
	private Map<Integer, Integer> dispensedNotes;
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Integer getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(Integer withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public Map<Integer, Integer> getDispensedNotes() {
		return dispensedNotes;
	}
	public void setDispensedNotes(Map<Integer, Integer> dispensedNotes) {
		this.dispensedNotes = dispensedNotes;
	}
	
	public ATMTransaction() {
		// TODO Auto-generated constructor stub
	}
	public ATMTransaction(Account account, Integer withdrawAmount, Map<Integer, Integer> dispensedNotes) {
		super();
		this.account = account;
		this.withdrawAmount = withdrawAmount;
		this.dispensedNotes = dispensedNotes;
	}
	@Override
	public String toString() {
		return "ATMTransaction [account=" + account + ", withdrawAmount=" + withdrawAmount + ", dispensedNotes="
				+ dispensedNotes + "]";
	}	
	
}
