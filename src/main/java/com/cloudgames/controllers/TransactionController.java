package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.Transaction;
import com.cloudgames.entities.TransactionType;
import com.cloudgames.services.TransactionService;

@Controller
@RequestMapping("accounts")
public class TransactionController {

	@Autowired
	private TransactionService ts;
	
	@GetMapping
	@ResponseBody
	public List<Transaction> getAllTransactions() {
		return ts.findAll();
	}
	
	@GetMapping("{status}")
	public Transaction findPending(@PathVariable int status) {
		return ts.findAllPending(status);
	}
	
	@GetMapping("{status}")
	public Transaction findCompleted(@PathVariable int status) {
		return ts.findAllCompleted(status);
	}
	
	@GetMapping("{ttype}")
	public Transaction findByType(@PathVariable TransactionType ttype) {
		return ts.findType(ttype);
	}
	
	@GetMapping("{status}")
	public Transaction findByAccount(@PathVariable Account acc) {
		return ts.findAccount(acc);
	}
	
	@PutMapping
	public Transaction update(@RequestBody Transaction t) {
		return ts.update(t);
	}
	
	@PostMapping
	public Transaction save(@RequestBody Transaction t) {
		return ts.save(t);
	}
}
