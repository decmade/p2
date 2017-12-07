package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.Transaction;
import com.cloudgames.entities.TransactionType;
import com.cloudgames.repositories.TransactionRepo;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepo tr;

	public Transaction save(Transaction t) {
		return tr.save(t);
	}
	
	public Transaction update(Transaction t) {
		return tr.update(t);
	}
	
	public List<Transaction> findAll() {
		return tr.findAll();
	}
	
	public Transaction findType(@PathVariable TransactionType ttype) {
		return tr.findType(ttype);
	}
	
	public Transaction findAllPending(@PathVariable int status) {
		return tr.findAllPending(status);
	}
	
	public Transaction findAllCompleted(@PathVariable int status) {
		return tr.findAllCompleted(status);
	}
	
	public Transaction findAccount(@PathVariable Account acc) {
		return tr.findAccount(acc);
	}
}
