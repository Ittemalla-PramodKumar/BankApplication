package com.example.BankApplication.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BankApplication.Model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Optional<Customer> findByCustomerId(int id);

	//@Query("SELECT * FROM Customer where name=?1")
	public Customer findByName(String name);

}
