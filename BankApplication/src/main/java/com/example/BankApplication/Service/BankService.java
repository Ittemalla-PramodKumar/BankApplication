package com.example.BankApplication.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.BankApplication.Dto.AccountCreationAcknowledgement;
import com.example.BankApplication.Dto.AccountCreationRequest;
import com.example.BankApplication.Exception.AccountNotFountException;
import com.example.BankApplication.Exception.CustomerNotFountException;
import com.example.BankApplication.Model.Account;
import com.example.BankApplication.Model.Branch;
import com.example.BankApplication.Model.Customer;
import com.example.BankApplication.Model.Status;
import com.example.BankApplication.Repo.AccountRepository;
import com.example.BankApplication.Repo.BranchRepo;
import com.example.BankApplication.Repo.CustomerRepository;
import com.example.BankApplication.Util.Utility;


@Service
public class BankService {

	@Autowired
	AccountRepository accRepo;
	
	@Autowired
	CustomerRepository cusRepo;
	
	@Autowired
	BranchRepo branchRepo;
	
	public Customer saveCustomer(Customer customer) {
		Customer cust = new Customer();
	//	Customer customerInfo = customer.getCustomerId();
		cust.setCustomerId(customer.getCustomerId());
		cust.setAddress(customer.getAddress());
		cust.setAge(customer.getAge());
		cust.setMobile(customer.getMobile());
		cust.setName(customer.getName());
		cust.setCreatedDate(new Date());
		cust.setUpdateDate(new Date());
		cust.setStatus(Status.CREATED);
		
		return  cusRepo.save(cust);
		
	}
	public AccountCreationAcknowledgement createAccountForCustomer(AccountCreationRequest request) {
		
		Customer customerInfo = request.getCustomerInfo();
		Account accountInfo = request.getAccountInfo();
		//This Method will throw exception on lower limit
		Utility.validateInitialDeposite(accountInfo.getBalance());
		
		customerInfo.setCreatedDate(new Date());
		customerInfo.setUpdateDate(new Date());
		customerInfo.setStatus(Status.CREATED);
		customerInfo = cusRepo.save(customerInfo);
		 
		//This Method will throw exception on lower limit
		accountInfo.setIntrestRate(Utility.getInterestRateForAccountType(accountInfo.getType()));
		accountInfo.setCustomerId(customerInfo.getCustomerId());
		accountInfo.setStatus(Status.CREATED);
		accRepo.save(accountInfo);
		
		return new AccountCreationAcknowledgement("Account_Created",customerInfo);
	}
	
	
	public AccountCreationRequest createAccountForExistingCustomer(Account accountDetails) {
		
		Customer customer = cusRepo.findById(accountDetails.getCustomerId()).get();
		System.out.println(accountDetails);
		if (customer != null) {
			Utility.validateInitialDeposite(accountDetails.getBalance());
			Account account = new Account();
			account.setCustomerId(accountDetails.getCustomerId());
			account.setType(accountDetails.getType());
			account.setStatus(Status.CREATED);
			account.setBalance(accountDetails.getBalance());
			account.setBalance(accountDetails.getBalance());
			account.setIntrestRate(Utility.getInterestRateForAccountType(accountDetails.getType()));
			accRepo.save(account);
			return new AccountCreationRequest(customer,account);
			} else {
				
				throw new CustomerNotFountException("No customer present for given id: "+accountDetails.getCustomerId());
		
			}
	}	
		public List<Customer> fetchAllCustomer(){
			return cusRepo.findAll();
		}
		
		public Customer findCustomerById(int id) {
			return cusRepo.findById(id).isPresent()?cusRepo.findById(id).get():null;	
		}
		
		public Customer deleteCustomer(int id) {
			Customer customer = cusRepo.findById(id).get();
			if (customer != null && customer.getStatus() != Status.CREATED) {
				customer.setStatus(Status.DELETED);
				//when customer deleted accounts needs to be closed
				
				accRepo.findAccountForCustomerId(id).forEach(acc -> {
				acc.setStatus(Status.CLOSED);
				accRepo.save(acc);
				
				//eventRepo.save(new EventLog("Account "+ acc.getAccountId()+" Closed"));
				});
				return cusRepo.save(customer);
				
			} else {
				return null;
			}
		}
			
	/*	public Customer findCustomerByName(String name , Customer details) {
			Customer cust = cusRepo.findByName(name);
			details.setAge(cust.getAge());
			details.setMobile(cust.getMobile());
			details.setAddress(cust.getAddress());
			details.setCustomerId(cust.getCustomerId());
			details.setStatus(Status.CREATED);
			 cusRepo.save(details);
			 return details;
			 
			
		}*/
		public Customer updateCustomerDetails(int id,Customer customerDetails) {
			return cusRepo.findById(id).map(cust -> {
				cust.setAge(customerDetails.getAge());
				cust.setName(customerDetails.getName());
				cust.setMobile(customerDetails.getMobile());
				cust.setAddress(customerDetails.getAddress());
				cust.setUpdateDate(new Date());
				cust.setStatus(Status.UPDATED);
				cusRepo.save(cust);
				return cust;
			}).orElseThrow(() -> {
				throw new CustomerNotFountException("Updation fial for Customer id: " +id);
			});
		}
		//Accounts
		
		public Account saveAccount(Account account) {
			Account acc = new Account();
			acc.setAccountId(account.getAccountId());
			acc.setBalance(account.getBalance());
			acc.setIntrestRate(Utility.getInterestRateForAccountType(account.getType()));
			acc.setStatus(Status.CREATED);
			acc.setType(account.getType());
			acc.setCustomerId(account.getCustomerId());
			acc.setBranch(account.getBranch());
			return accRepo.save(acc);
			
		}
		public List<Account> fetchAllAccount(){
			return accRepo.findAll();
		}
			
		public Account findAccountById(int id) {
			return accRepo.findById(id).isPresent()?accRepo.findById(id).get():null;
		}
			
		public Account updateAccountDetails(int id, Account accountDetails) {
			return accRepo.findById(id).map(acc -> {
				acc.setBalance(accountDetails.getBalance());
			//	acc.setBranch(accountDetails.getBranch());
				acc.setType(accountDetails.getType());
				acc.setIntrestRate(Utility.getInterestRateForAccountType(accountDetails.getType()));
				acc.setStatus(Status.UPDATED);
				accRepo.save(acc);
				return acc;
			}).orElseThrow(() -> {
				throw new AccountNotFountException("Account updation failed for id: "+ id);
			});
		}
			
		public Account deleteAccount(int id) {
			Account account = accRepo.findById(id).get();
			if (account != null && account.getStatus() != Status.DELETED) {
				account.setStatus(Status.DELETED);
				return accRepo.save(account);
			} else {
				return null;
			}
		}
		
		//Branch
		public Branch addBranch(Branch branch) {
			Branch bch = new Branch();
			bch.setBranchid(branch.getBranchid());
			bch.setAddress(branch.getAddress());
			bch.setPhone(branch.getPhone());
			
			return branchRepo.save(bch);
		}
		

}