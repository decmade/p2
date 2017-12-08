package com.cloudgames.repositories.interfaces;

import org.springframework.web.bind.annotation.PathVariable;

import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.AccountInterface;

public interface AccountRepositoryInterface extends RepositoryInterface<AccountInterface> {
	
	public AccountInterface fetchByOwner(@PathVariable User u);
	public AccountInterface fetchById(@PathVariable int id);
}
