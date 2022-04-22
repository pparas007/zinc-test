package com.test.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.test.demo.model.Account;

public interface AccountRepository  extends CrudRepository<Account, Integer>{

	public List<Account> findByAccountNumber(Long accountNumber);
}
