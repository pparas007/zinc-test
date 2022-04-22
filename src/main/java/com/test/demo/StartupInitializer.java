package com.test.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.test.demo.model.ATMMachine;
import com.test.demo.model.Account;
import com.test.demo.repository.ATMRepository;
import com.test.demo.repository.AccountRepository;
import com.test.demo.service.AccountService;

@Component
public class StartupInitializer {
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private ATMRepository atmRepository;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * Start up mehod to setup dummy data for testing
	 */
    @EventListener
    public void appReady(ApplicationReadyEvent event) {
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
}
