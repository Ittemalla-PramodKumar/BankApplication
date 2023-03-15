package com.example.BankApplication.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.BankApplication.Dto.AccountCreationAcknowledgement;
import com.example.BankApplication.Dto.AccountCreationRequest;
import com.example.BankApplication.Exception.CustomerNotFountException;
import com.example.BankApplication.Exception.InsufficientBalanceException;
import com.example.BankApplication.Model.Account;
import com.example.BankApplication.Model.Branch;
import com.example.BankApplication.Model.Customer;
import com.example.BankApplication.Service.BankService;

@RestController
@EnableTransactionManagement
public class BankController {
	
	@Autowired
	BankService bankService;
	
	@PostMapping("/addCustomer")
	public Customer addCustomer(@RequestBody Customer customer) {
		return bankService.saveCustomer(customer);
	}
	@PostMapping("/createAccount")
	public AccountCreationAcknowledgement createAccForCust(@RequestBody AccountCreationRequest request) {
		try {
			AccountCreationAcknowledgement ackd = bankService.createAccountForCustomer(request);
			return ackd;
		} catch (InsufficientBalanceException e) {
			e.printStackTrace();
			return new AccountCreationAcknowledgement("INSUFFICIENT_BALANCE_EXCEPTION:CHECK_EVENTLOG",request.getCustomerInfo());
		} catch (Exception e) {
			e.printStackTrace();
			return new AccountCreationAcknowledgement("EXCEPTION_OCCURED:CHECK_EVENTLOG",request.getCustomerInfo());
		}
	}
	
	@PostMapping("/createAccForExistingCustomer")
	public ResponseEntity<AccountCreationRequest> createAccForExistingCustomer(@RequestBody Account accountDetails){
		try {
		AccountCreationRequest ack = bankService.createAccountForExistingCustomer(accountDetails);
		return ResponseEntity.ok(ack);
		} catch(CustomerNotFountException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}		
	}
	
	@GetMapping("/getAllCustomers")
	public ResponseEntity<List<Customer>> getAllCustomers(){
		return ResponseEntity.ok(bankService.fetchAllCustomer());
		
	}
	
	
	@PutMapping("/updateCustomer/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable int id,@RequestBody Customer customerDetails){
		Customer updateCustomer = bankService.updateCustomerDetails(id, customerDetails);
		if (updateCustomer != null) {
			return ResponseEntity.ok(updateCustomer);
			
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<Customer> deleteCustomer(@PathVariable int id){
		Customer delete = bankService.deleteCustomer(id);
		if (delete != null) {
			return ResponseEntity.accepted().body(delete);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Account
	@PostMapping("/addAccount")
		public Account addAccount(@RequestBody Account account) {
		return bankService.saveAccount(account);
	}
	@GetMapping("/getAllAccounts")
	public ResponseEntity<List<Account>> getAllAccounts(){
		return ResponseEntity.ok(bankService.fetchAllAccount());
	}
	
	@GetMapping("/getAccount/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable int id){
		Account acc = bankService.findAccountById(id);
		if (acc != null) {
			return ResponseEntity.ok(acc);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/updateAccount/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable int id,@RequestBody Account accountDetails){
		Account update = bankService.updateAccountDetails(id, accountDetails);
		if (update != null) {
			return ResponseEntity.ok(update);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/deleteAccount/{id}")
	public ResponseEntity<Account> deleteAccountById(@PathVariable int id){
		Account delete = bankService.deleteAccount(id);
		if (delete != null) {
			return ResponseEntity.accepted().body(delete);
		} else {
			return ResponseEntity.notFound().build();
		}
	}	
//	//Branch
//	@PostMapping("/addBranch")
//	public Branch addBranch(@RequestBody Branch branch) {
//		return bankService.addBranch(branch);
//	}
}