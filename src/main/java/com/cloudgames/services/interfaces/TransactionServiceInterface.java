package com.cloudgames.services.interfaces;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.interfaces.TransactionInterface;

public interface TransactionServiceInterface extends ServiceInterface<TransactionInterface> {

	TransactionInterface fetchByAccount(Account acc);

}
