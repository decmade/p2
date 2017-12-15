package com.cloudgames.entities.interfaces;

import com.cloudgames.entities.TransactionType;

public interface TransactionInterface extends EntityInterface{

	public double getAmount();
	
	public void setAmount(double amount);
	
	public TransactionType getType();
	
	public void setType(TransactionType transactionType);

	String getCreated();

	void setCreated(String created);
}
