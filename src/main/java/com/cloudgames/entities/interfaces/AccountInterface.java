package com.cloudgames.entities.interfaces;

public interface AccountInterface extends EntityInterface {
	/*
	 * Retrieve balance of account
	 */
	public double getBalance();
	/*
	 * Set balance of account
	 */
	public void setBalance(double balance);
}
