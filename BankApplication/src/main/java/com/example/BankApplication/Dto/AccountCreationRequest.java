package com.example.BankApplication.Dto;

import com.example.BankApplication.Model.Account;
import com.example.BankApplication.Model.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AccountCreationRequest {

	private Customer customerInfo;
	private Account accountInfo;
}
