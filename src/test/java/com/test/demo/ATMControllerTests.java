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
import com.test.demo.model.ATMMachine;
import com.test.demo.model.ATMTransaction;
import com.test.demo.model.Account;
import com.test.demo.repository.ATMRepository;
import com.test.demo.service.AccountService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Before;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class ATMControllerTests {
	
	@Autowired
	private ATMController atmController;
	
	@Autowired
	private ATMRepository atmRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	//@Autowired
	private MockMvc mockMvc;
	
	
	@BeforeEach
	public void setUp() {
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	    
	    Account account1=new Account(120456789L, "1234", 800.0, 200.0);
	    Account account2=new Account(220456789L, "1234", 800.0, 200.0);
	    Account account3=new Account(320456789L, "1234", 800.0, 200.0);
	    Account account4=new Account(420456789L, "1234", 800.0, 200.0);
    	
    	accountService.addNewAccount(account1);
    	accountService.addNewAccount(account2);
    	accountService.addNewAccount(account3);
    	accountService.addNewAccount(account4);
    	
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
		Assertions.assertThat(atmController).isNotNull();
	}
	
	@Test
	@Order(2)
	public void testAccountBalanceEnquiry() throws Exception {
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(120456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);

	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    String requestJson=mapper.writeValueAsString(atmTransaction);
	    
	    mockMvc.perform(post("/accounts/balance")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(requestJson) 
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.account.balance").value("800.0"));
	    
	}
	
	@Test
	@Order(3)
	public void test_BalanceAndWithdrawableAmount_AfterWithdrawingAmount() throws Exception {
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(120456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);
		atmTransaction.setWithdrawAmount(100);

	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    String requestJson=mapper.writeValueAsString(atmTransaction);
	    
	    mockMvc.perform(post("/accounts/withdraw")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(requestJson) 
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.account.balance").value("700.0"))
	    		.andExpect(jsonPath("$.account.withdrawableAmount").value("900.0"));
	    
	}
	
	@Test
	@Order(4)
	public void test_WithdrawingMoreCashThanWithdrawableAmount_AfterWithdrawingAmount() throws Exception {
		ATMTransaction atmTransaction = new ATMTransaction();
		Account account=new Account();
		account.setAccountNumber(220456789L);
		account.setPin("1234");
		atmTransaction.setAccount(account);
		atmTransaction.setWithdrawAmount(1200);

	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    String requestJson=mapper.writeValueAsString(atmTransaction);
	    
	    mockMvc.perform(post("/accounts/withdraw")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(requestJson) 
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isBadRequest())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").
						value("You do not have enough balance in the account. (Total WithdrawableAmount amount: 1000.0, Entered amount: 1200.) Please try a different amount"));
	    
	}
	
}
