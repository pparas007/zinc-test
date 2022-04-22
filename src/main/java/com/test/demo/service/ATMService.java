package com.test.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.demo.exception.AccountNumberNotPresentException;
import com.test.demo.exception.CanNotDispeseTheGivenAmountException;
import com.test.demo.exception.CustomException;
import com.test.demo.exception.InvalidWithdrawalAmountException;
import com.test.demo.exception.NotEnoughBalanceException;
import com.test.demo.exception.PinNotEnteredException;
import com.test.demo.exception.PinNotMatchingException;
import com.test.demo.model.ATMMachine;
import com.test.demo.model.ATMTransaction;
import com.test.demo.model.Account;
import com.test.demo.repository.ATMRepository;
import com.test.demo.util.ATMUtility;
import com.test.demo.util.PasswordHashingUtility;

@Service
public class ATMService {
	@Autowired
	private ATMRepository atmRepository;
	
	@Autowired
	private AccountService accountService;
	
	//--------------------- service methods ---------------------
	/**
	 * Service method to make balance inquiry
	 *
	 * @param ATMTransaction contains account details accountNumber, pin 
	 * @return ATMTransaction   contains details accountNumber, balance, overdraft, withdrawableAmount
	 */
	public ATMTransaction enquireBalance(ATMTransaction atmTransaction) throws CustomException {
		//input validations
		validateAtmTransaction(atmTransaction);
		
		Account fetchedAccount=accountService.getAcountDetails(atmTransaction.getAccount().getAccountNumber());
		
		//do not expose account's id and pin to client
		fetchedAccount.setId(null);
		fetchedAccount.setPin(null);
		atmTransaction.setAccount(fetchedAccount);
		
		return atmTransaction;
	}
	
	/**
	 * Service method to withdraw cash
	 *
	 * @param ATMTransaction contains account details accountNumber, pin 
	 * @return ATMTransaction   contains details accountNumber, balance, overdraft, withdrawableAmount. Also contains the map structure for the notes dispensed. 
	 */
	public ATMTransaction withdrawAmount(ATMTransaction atmTransaction) throws CustomException {
		//input validations
		validateAtmTransactionForWithdrawal(atmTransaction);
		
		Integer withdrawAmount= atmTransaction.getWithdrawAmount();
		
		//currently only one ATM machine is present in the system. 
		//This can be changed in future to fetch ATM machine from which the request is made. Machine id can be passed in the request.
		ATMMachine atmMachine=atmRepository.findAll().iterator().next();
		
		//findDispensableNotes is the utility method which receives atmMachine and withdrawAmount details, and returns details of notes dispensed in form of Map
		Map<Integer, Integer> dispensedNotes=ATMUtility.findDispensableNotes(atmMachine, withdrawAmount);
		//if 'null' is returned, that means ATM can not dispense the notes.
		if(dispensedNotes==null)
			throw new CanNotDispeseTheGivenAmountException();
		
		//deduct the amount from the account balance
		Account fetchedAccount=accountService.getAcountDetails(atmTransaction.getAccount().getAccountNumber());
		accountService.withdrawAmount(fetchedAccount, atmTransaction.getWithdrawAmount());
		
		//deduct the amount from the ATM balance
		dispenseCash(atmMachine, dispensedNotes);
		
		//do not expose account's id and pin to client
		fetchedAccount.setId(null);
		fetchedAccount.setPin(null);
		atmTransaction.setAccount(fetchedAccount);
		atmTransaction.setDispensedNotes(dispensedNotes);
		
		return atmTransaction;
	}
	
	/**
	 * Service method to withdraw cash
	 *
	 * Deduct the cash from the atm i.e. update the number of notes
	 *
	 * @param atmMachine contains atmMachine details like total cash available and notes available
	 * @param dispensedNotes map for the cash to be deducted from the atm 
	 */
	private void dispenseCash(ATMMachine atmMachine, Map<Integer, Integer> dispensedNotes) {
		for(Integer denomination: dispensedNotes.keySet()) {
			int newNotes=atmMachine.getDenominationNotesMap().get(denomination) - dispensedNotes.get(denomination);
			
			//deduct the notes dispensed from the atm
			atmMachine.getDenominationNotesMap().put(denomination, newNotes);
		}
		atmRepository.save(atmMachine);
	}

	//--------------------- validations ---------------------
	/**
	 * input validation method
	 *
	 * @param ATMTransaction contains account details accountNumber, pin 
	 * @throws CustomException including InvalidWithdrawalAmountException, NotEnoughBalanceException
	 */
	private void validateAtmTransactionForWithdrawal(ATMTransaction atmTransaction) throws CustomException {
		validateAtmTransaction(atmTransaction);
		
		//validate WithdrawAmount to be greater than 0
		if(atmTransaction.getWithdrawAmount()==null || atmTransaction.getWithdrawAmount()<=0)
			throw new InvalidWithdrawalAmountException();
		
		Account fetchedAccount=accountService.getAcountDetails(atmTransaction.getAccount().getAccountNumber());
		
		//validate the amout to be withdraw is less than the allowed withdrawable amount
		if(atmTransaction.getWithdrawAmount() > fetchedAccount.getWithdrawableAmount())
			throw new NotEnoughBalanceException(atmTransaction.getWithdrawAmount(), fetchedAccount.getWithdrawableAmount());
		
	}
	
	/**
	 * input validation method
	 *
	 * @param ATMTransaction contains account details accountNumber, pin 
	 * @throws CustomException including AccountNumberNotPresentException, PinNotEnteredException, PinNotMatchingException
	 */
	private void validateAtmTransaction(ATMTransaction atmTransaction) throws CustomException {
		//validate the account nunmber
		if(atmTransaction==null || atmTransaction.getAccount()==null 
				|| atmTransaction.getAccount().getAccountNumber()==null)
			throw new AccountNumberNotPresentException();
		
		validatePassword(atmTransaction.getAccount());
	}
	
	/**
	 * input validation method
	 *
	 * @param ATMTransaction contains account details accountNumber, pin 
	 * @throws CustomException including PinNotEnteredException, PinNotMatchingException
	 */
	private void validatePassword(Account account) throws CustomException {
		//validate that the pin is entered
		if(account.getPin()==null)
			throw new PinNotEnteredException();
		
		Account fetchedAccount=accountService.getAcountDetails(account.getAccountNumber());
		
		//find the hash of the input pin
		String hashedPin=PasswordHashingUtility.calculatePasswordHash(account.getPin().toString());
		
		//match the hash of the pin
		if(!hashedPin.equals(fetchedAccount.getPin()))
			throw new PinNotMatchingException();
	}
	
	//--------------------- dummy method for testing---------------------
	public ATMMachine getMachineInfo() {
		ATMMachine atmMachine= atmRepository.findAll().iterator().next();
		atmMachine.calculateTotalCashAvailable();
		
		return atmMachine;
	}
}
