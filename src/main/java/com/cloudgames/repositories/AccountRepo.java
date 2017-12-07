package com.cloudgames.repositories;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.User;

public interface AccountRepo {

	public Account save(Account acc);
	public Account update(Account acc);
	public List<Account> findAll();
	public Account findByOwner(@PathVariable User u);
}
