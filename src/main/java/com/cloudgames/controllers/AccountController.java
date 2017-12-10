package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.AccountInterface;
import com.cloudgames.services.AccountService;
import com.cloudgames.services.interfaces.AccountServiceInterface;

@RestController("account-controller")
@RequestMapping("accounts")
public class AccountController extends AbstractController<AccountInterface, Account>{

	@Autowired
	@Qualifier("account-service")
	private AccountServiceInterface service;
	
	@Override
	@GetMapping("{id:[0-9]+}")
	public AccountInterface get(@PathVariable int id) {
		log.debug("retrieving Account with ID: " + id);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<AccountInterface> getAll() {
		log.debug("retrieving all Accounts");
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public AccountInterface save(@RequestBody Account acc) {
		if (acc.getId() > 0) {
			log.debug("updating Account with ID: " + acc.getId());
		} else {
			log.debug("adding new account");
		}
		
		return this.service.save(acc);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody Account acc) {
		log.debug("removing account with ID: " + acc.getId());
		
		this.service.delete(acc);
	}

	
}
