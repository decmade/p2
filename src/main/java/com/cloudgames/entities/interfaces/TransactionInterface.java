package com.cloudgames.entities.interfaces;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.TransactionType;

public interface TransactionInterface extends EntityInterface{

	public double getAmount();
	
	public void setAmount(double amount);
	
	public Account getAcc();
	
	public void setAcc(Account acc);
	
	public TransactionType getTransactionType();
	
	public void setTransactionType(TransactionType transactionType);
}
