package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudgames.entities.Account;

@Service
public class AccountService {

	@Autowired
	private AccountRepo ar;

	public Account save(Account acc) {
		return ar.save(acc);
	}
	
	public Account update(Account acc) {
		return ar.update(acc);
	}

	public List<Account> findAll() {
		return ar.findAll();
	}
	
	public Account findByOwner(@PathVariable User u) {
		return ar.findByOwner(u);
	}
	
}
