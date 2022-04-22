package com.test.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.test.demo.controller.ATMController;
import com.test.demo.exception.AccountNumberNotPresentException;
import com.test.demo.exception.CustomException;
import com.test.demo.exception.InvalidWithdrawalAmountException;
import com.test.demo.exception.NotEnoughBalanceException;
import com.test.demo.model.ATMMachine;
import com.test.demo.model.ATMTransaction;
import com.test.demo.model.Account;
import com.test.demo.repository.ATMRepository;
import com.test.demo.service.ATMService;
import com.test.demo.service.AccountService;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.aspectj.lang.annotation.Before;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class ATMServiceTests {
	
	@Autowired
	private ATMRepository atmRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ATMService atmService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	    
	    Account account1=new Account(123456789L, "1234", 800.0, 200.0);
    	Account account2=new Account(987654321L, "4321", 1230.0, 150.0);
    	
    	accountService.addNewAccount(account1);
    	accountService.addNewAccount(account2);
    	
    	
    	
    	Map<Integer, Integer> denominationNotesMap=new HashMap<Integer, Integer>();
    	denominationNotesMap.put(5, 20);
    	denominationNotesMap.put(10, 30);
    	denominationNotesMap.put(20, 30);
    	denominationNotesMap.put(50, 10);
    	ATMMachine atm1=new ATMMachine(denominationNotesMap);
    	
    	atmRepository.save(atm1);
	}
	
	
	@Test
	@Order(1)
	public void contextLoads() throws Exception {
		Assertions.assertThat(atmService).isNotNull();
	}
	
	@Test
	@Order(2)
	public void testAccountBalanceEnquiry() throws Exception, CustomException {
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(123456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);

		ATMTransaction returnedAtmTransaction=atmService.enquireBalance(atmTransaction);
		assertEquals(800, returnedAtmTransaction.getAccount().getBalance());
	    
	}
	
	@Test
	@Order(3)
	public void test_IdAndPinNotExposed_AccountBalanceEnquiry() throws Exception, CustomException {
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(123456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);

		ATMTransaction returnedAtmTransaction=atmService.enquireBalance(atmTransaction);
		assertEquals(null, returnedAtmTransaction.getAccount().getId());
		assertEquals(null, returnedAtmTransaction.getAccount().getPin());
	}
	
	@Test
	@Transactional
	@Order(4)
	public void test_BalanceAndWithdrawableAmount_AfterWithdrawingAmount() throws Exception, CustomException {
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(123456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);
		atmTransaction.setWithdrawAmount(100);
		
		
		ATMTransaction returnedAtmTransaction=atmService.withdrawAmount(atmTransaction);
		
		assertEquals(700, returnedAtmTransaction.getAccount().getBalance());
	    assertEquals(900	, returnedAtmTransaction.getAccount().getWithdrawableAmount());
	    
	    int dispensedNotesof50=atmTransaction.getDispensedNotes().get(50);
	    assertEquals(dispensedNotesof50, 2);
	}
	
	@Test
	@Transactional
	@Order(4)
	public void test_AccountNumberNotPresentExceptionThrown_AfterWithdrawingAmount() throws Exception, CustomException {
		
	    AccountNumberNotPresentException exception1 = assertThrows(AccountNumberNotPresentException.class, () -> {
	    	atmService.withdrawAmount(null);
	    });
	    
	    ATMTransaction atmTransaction = new ATMTransaction();
	    AccountNumberNotPresentException exception2 = assertThrows(AccountNumberNotPresentException.class, () -> {
	    	atmService.withdrawAmount(atmTransaction);
	    });
	    
	    Account account=new Account();
	    atmTransaction.setAccount(account);
	    AccountNumberNotPresentException exception3 = assertThrows(AccountNumberNotPresentException.class, () -> {
	    	atmService.withdrawAmount(atmTransaction);
	    });
	}
	
	@Test
	@Transactional
	@Order(4)
	public void test_InvalidWithdrawalAmountExceptionThrown_AfterWithdrawingAmount() throws Exception, CustomException {
		
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(123456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);
		atmTransaction.setWithdrawAmount(null);
		
		InvalidWithdrawalAmountException exception1 = assertThrows(InvalidWithdrawalAmountException.class, () -> {
	    	atmService.withdrawAmount(atmTransaction);
	    });
	    
		atmTransaction.setWithdrawAmount(-1);
		InvalidWithdrawalAmountException exception2 = assertThrows(InvalidWithdrawalAmountException.class, () -> {
	    	atmService.withdrawAmount(atmTransaction);
	    });
	    
	}
	
	@Test
	@Transactional
	@Order(5)
	public void test_NotEnoughBalanceExceptionThrown_AfterWithdrawingAmount() throws Exception, CustomException {
		
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(123456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);
		atmTransaction.setWithdrawAmount(1300);
		
		NotEnoughBalanceException exception1 = assertThrows(NotEnoughBalanceException.class, () -> {
	    	atmService.withdrawAmount(atmTransaction);
	    });
	    
	}
}
