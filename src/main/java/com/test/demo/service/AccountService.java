package com.test.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.demo.exception.CustomException;
import com.test.demo.exception.NonExistingAccountException;
import com.test.demo.model.Account;
import com.test.demo.repository.AccountRepository;
import com.test.demo.util.PasswordHashingUtility;

@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	
	/**
	 * Service method to get account details
	 *
	 * @param  accountNumber 
	 * @return Account  contains account details
	 * @throws CustomException including NonExistingAccountException
	 */
	public Account getAcountDetails(Long accountNumber) throws CustomException {
		List<Account> accounts= accountRepository.findByAccountNumber(accountNumber);
		if(accounts==null || accounts.size()==0)
			throw new NonExistingAccountException();
		
		Account account= accounts.get(0);
		//calculate withdrawable amount
		account.setWithdrawableAmount(account.getBalance()+account.getOverdraft());
		
		return account;
	}
	
	/**
	 * Service method to deduct withdrawAmount from the account
	 *
	 * @param  Account 
	 * @param  withdrawAmount   
	 */
	public void withdrawAmount(Account fetchedAccount, Integer withdrawAmount) {
		fetchedAccount.setBalance(fetchedAccount.getBalance() - withdrawAmount);
		//recalculate withdrawableAmount
		fetchedAccount.setWithdrawableAmount(fetchedAccount.getBalance()+fetchedAccount.getOverdraft());
		accountRepository.save(fetchedAccount);
	}
	
	/**
	 * Service method to add new account
	 *
	 * @param  Account 
	 */
	public void addNewAccount(Account account) {
		//calculate hash of the password
		String hashedPin=PasswordHashingUtility.calculatePasswordHash(account.getPin().toString());
		
		account.setPin(hashedPin);
		accountRepository.save(account);
	}
}
