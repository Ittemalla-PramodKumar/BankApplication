package com.example.BankApplication.Exception;

public class InsufficientBalanceException extends RuntimeException{
	
	public InsufficientBalanceException(String msg) {
		super(msg);
	}

}
