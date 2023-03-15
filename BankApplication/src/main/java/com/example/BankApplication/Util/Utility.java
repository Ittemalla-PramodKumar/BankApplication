package com.example.BankApplication.Util;

import com.example.BankApplication.Exception.InsufficientBalanceException;

public class Utility {
	
	public static boolean validateInitialDeposite(Double accountOpeningBalance) {
		
		if(accountOpeningBalance <10000d) {
			throw new InsufficientBalanceException("Accpunt Opening Balance needs to be greater than 10000!!");
		} else {
			return true;
		}
	}
	
	public static Float getInterestRateForAccountType(String accountType) {
		
		if(accountType.equals("SAVINGS")) {
			return 3.00f;
		}else {
			return 2.00f;
		}
		
	}

}
