package com.example.BankApplication.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BankApplication.Model.Branch;

public interface BranchRepo extends JpaRepository<Branch, Integer> {

}
