package com.test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.exception.CustomException;
import com.test.demo.exception.NonExistingAccountException;
import com.test.demo.exception.PinNotEnteredException;
import com.test.demo.exception.PinNotMatchingException;
import com.test.demo.model.ATMMachine;
import com.test.demo.model.ATMTransaction;
import com.test.demo.model.Account;
import com.test.demo.service.ATMService;

@RestController
public class ATMController {

	@Autowired
	private ATMService atmService;
	
	/**
	 * Rest controller for balance inquiry
	 *
	 * @param ATMTransaction contains account details accountNumber, pin
	 * @return ATMTransaction   contains details accountNumber, balance, overdraft, withdrawableAmount
	 */
	@PostMapping("/accounts/balance")
	public ATMTransaction enquireBalance(@RequestBody ATMTransaction atmTransaction) throws CustomException {
		return atmService.enquireBalance(atmTransaction);
	}
	
	/**
	 * Rest controller for requesting cash withdraw
	 *
	 * @param ATMTransaction contains account details accountNumber, pin & amount to be withdrawn
	 * @return ATMTransaction   contains details accountNumber, balance, overdraft, withdrawableAmount. Also contains the map structure for the notes dispensed.
	 */
	@PostMapping("/accounts/withdraw")
	public ATMTransaction withdrawAmount(@RequestBody ATMTransaction atmTransaction) throws CustomException {
		return atmService.withdrawAmount(atmTransaction);
	}
	
	
	//--------------------- dummy method for testing---------------------
	
	/**
	 * Rest controller for requesting atm machine info
	 *
	 * This method is not part of the requirements. It is just for testing purpose.
	 *
	 * @param 
	 * @return ATMMachine  contains details of notes available with the ATM machine and total available cash
	 */
	@GetMapping("/atmmachine")
	public ATMMachine getMachineInfo() throws CustomException {
		return atmService.getMachineInfo();
	}
}
