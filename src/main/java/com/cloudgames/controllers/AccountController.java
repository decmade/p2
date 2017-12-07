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
import com.revature.services.AccountService;

@Controller
@RequestMapping("accounts")
public class AccountController {

	@Autowired
	private AccountService as;
	
	@GetMapping
	@ResponseBody
	public List<Account> getAllAccounts() {
		return as.findAll();
	}
	
	@GetMapping("{id}")
	public Account findByOwner(@PathVariable User u) {
		return as.findByOwner(u);
	}
	
	@PutMapping
	public Account update(@RequestBody Account acc) {
		return as.update(u);
	}
	
	@PostMapping
	public Account save(@RequestBody Account acc) {
		return as.save(acc);
	}
}
