package com.test.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.test.demo.model.ATMMachine;

public interface ATMRepository  extends CrudRepository<ATMMachine, Integer>{
	
}
