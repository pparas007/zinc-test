package com.test.demo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.demo.model.ATMMachine;

public class ATMUtility {
	
	/**
	 * Utility method to calculate minimum numbers of notes to be dispensed
	 *
	 * @param  ATMMachine  details of machine including notes available
	 * @param  withdrawAmount
	 * @return dispensedNotes  map structure for the notes dispensed. 
	 */
	
	public static Map<Integer, Integer> findDispensableNotes(ATMMachine atmMachine, Integer withdrawAmount) {
		int [] denominations = new int[atmMachine.getDenominationNotesMap().keySet().size()];
		int [] notes =new int[denominations.length];
		Map<Integer, Integer> dispensedNotes=new HashMap<Integer, Integer>();
		
		int index=0;
		for(int denomination:atmMachine.getDenominationNotesMap().keySet()) {
			denominations[index++]=denomination;
		}
		
		index=0;
		for(int numberOfNotes:atmMachine.getDenominationNotesMap().values()) {
			notes[index++]=numberOfNotes;
		}
		
		//method to find out all the combinations to get withdraw amount
		List<Integer[]> combinations = generateCombinations(denominations, notes,  new int[denominations.length], withdrawAmount, 0);
		
		Integer[] selectedCombination=null;
        int minimumNumberOfNotes=(int)Double.POSITIVE_INFINITY;
        
        for (Integer[] combination : combinations){
            int numberOfNotes=0;
            for(int i:combination) {
            	numberOfNotes+=i;
            }
            
            //choose the combination with minimum number of notes
            if(numberOfNotes<minimumNumberOfNotes) {
            	selectedCombination=combination;
            	minimumNumberOfNotes=numberOfNotes;
            }
        }
        
        //if no combination is available, that means atm doesn't have the notes to dispense the withdrawAmount
        if(selectedCombination==null)
        	return null;
        
        //prepare the dispensedNotes map where 'denomination' is key, and 'number of notes' is the value
        for(int i=0;i<selectedCombination.length;i++) {
        	dispensedNotes.put(denominations[i], selectedCombination[i]);
        }
        
		return dispensedNotes;
	}
	
	/**
	 * Utility method to recursively find out all the combinations of notes that will generate the withdrawAmount 
	 *
	 * @param  denominations  list of denominations available
	 * @param  notes  number of notes available for each denomination
	 * @param  variation  array containing number of notes under each denominations. The variation will change for each iteration.
	 * @param  withdrawAmount 
	 * @param  position  index of the denomination to be changed under current iteration
	 * @return combinations  list of combinations 
	 */
	private static List<Integer[]> generateCombinations(int[] denominations, int[] notes, int[] variation, int withdrawAmount, int position){
        List<Integer[]> combinations = new ArrayList<>();
        int value = compute(denominations, variation);
        if (value < withdrawAmount){
            for (int i = position; i < denominations.length; i++) {
                if (notes[i] > variation[i]){
                    int[] newvariation = variation.clone();
                    newvariation[i]++;
                    List<Integer[]> newCombination = generateCombinations(denominations, notes, newvariation, withdrawAmount, i);
                    combinations.addAll(newCombination);
                }
            }
        } else if (value == withdrawAmount) {
        	combinations.add(copy(variation));
        }
        return combinations;
    }    
	
	/**
	 * Method to calculate total amount of the variation 
	 *
	 * @return totalAmount  computed totalAmount for the variation 
	 */
	private static int compute(int[] denominations, int[] variation){
        int totalAmount = 0;
        for (int i = 0; i < variation.length; i++) {
        	totalAmount += denominations[i] * variation[i];
        }
        return totalAmount;
    }    

	private static Integer[] copy(int[] variation){
        Integer[] ret = new Integer[variation.length];
        for (int i = 0; i < variation.length; i++) {
            ret[i] = variation[i];
        }
        return ret;
    }
}
