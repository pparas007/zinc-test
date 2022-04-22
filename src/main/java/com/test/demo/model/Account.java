package com.test.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Account {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private Long accountNumber;
	private String pin;
	private Double balance;
	private Double overdraft;
	
	@Transient
	private Double withdrawableAmount;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getOverdraft() {
		return overdraft;
	}
	public void setOverdraft(Double overdraft) {
		this.overdraft = overdraft;
	}
	public Double getWithdrawableAmount() {
		return withdrawableAmount;
	}
	public void setWithdrawableAmount(Double withdrawableAmount) {
		this.withdrawableAmount = withdrawableAmount;
	}
	public Account(Long accountNumber, String pin, Double balance, Double overdraft) {
		super();
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.balance = balance;
		this.overdraft = overdraft;
	}
	public Account() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", accountNumber=" + accountNumber + ", pin=" + pin + ", balance=" + balance
				+ ", overdraft=" + overdraft + "]";
	}
	
}
