package com.test.demo.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class ATMMachine {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ElementCollection
	private Map<Integer, Integer> denominationNotesMap;
	
	@JsonProperty
	@Transient
	private Double totalCashAvailable;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<Integer, Integer> getDenominationNotesMap() {
		return denominationNotesMap;
	}

	public void setDenominationNotesMap(Map<Integer, Integer> denominationNotesMap) {
		this.denominationNotesMap = denominationNotesMap;
	}

	public Double getTotalCashAvailable() {
		return totalCashAvailable;
	}

	public void setTotalCashAvailable(Double totalCashAvailable) {
		this.totalCashAvailable = totalCashAvailable;
	}

	@Override
	public String toString() {
		String printString="ATMMachine [id=" + id + ", denominationNotesMap=";
		
		final String denominationNotesMapString="";
		denominationNotesMap.entrySet().stream().forEach(e-> denominationNotesMapString.concat(e.toString()));
		printString=printString+denominationNotesMap;
		
		calculateTotalCashAvailable();
		printString+=", totalCashAvailable: "+totalCashAvailable +"]";
		
		return printString;
	}

	public ATMMachine(Map<Integer, Integer> denominationNotesMap) {
		super();
		this.denominationNotesMap = denominationNotesMap;
	}
	
	public ATMMachine() {
	}
	
	public void calculateTotalCashAvailable() {
		totalCashAvailable=(double) denominationNotesMap.entrySet().stream().map(e-> e.getKey()*e.getValue())
				.mapToInt(Integer::intValue).sum();
	}
}
