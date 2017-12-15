package com.cloudgames.entities.interfaces;

import java.util.List;

import com.cloudgames.entities.Transaction;

public interface AccountInterface extends EntityInterface {
	/*
	 * Retrieve balance of account
	 */
	public double getBalance();
	/*
	 * Set balance of account
	 */
	public void setBalance(double balance);
	List<Transaction> getTransactions();
	void setTransactions(List<Transaction> transactions);
}
