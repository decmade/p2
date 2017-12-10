package com.cloudgames.repositories.interfaces;

import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.AccountInterface;

public interface AccountRepositoryInterface extends RepositoryInterface<AccountInterface> {
	
	public AccountInterface fetchByOwner(User u);
	@Override
	public AccountInterface fetchById(int id);
}
