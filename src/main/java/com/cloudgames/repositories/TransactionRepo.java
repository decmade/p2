package com.cloudgames.repositories;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.Transaction;
import com.cloudgames.entities.TransactionType;

public interface TransactionRepo {
	
	public Transaction save(Transaction t);
	public Transaction update(Transaction t);
	public List<Transaction> findAll();
	public Transaction findType(@PathVariable TransactionType ttype);
	public Transaction findAllPending(@PathVariable int status);
	public Transaction findAllCompleted(@PathVariable int status);
	public Transaction findAccount(@PathVariable Account acc);
	
}
