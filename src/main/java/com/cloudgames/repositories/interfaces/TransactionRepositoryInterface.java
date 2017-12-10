package com.cloudgames.repositories.interfaces;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.interfaces.TransactionInterface;

public interface TransactionRepositoryInterface extends RepositoryInterface<TransactionInterface> {

	public TransactionInterface fetchByAccount(Account acc);
	
}
