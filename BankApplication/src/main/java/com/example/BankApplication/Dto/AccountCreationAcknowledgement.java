package com.example.BankApplication.Dto;

import com.example.BankApplication.Model.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AccountCreationAcknowledgement {
	
	private String status;
	private Customer customer;
}
