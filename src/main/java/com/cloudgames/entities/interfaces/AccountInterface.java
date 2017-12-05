package com.cloudgames.entities.interfaces;

import com.cloudgames.entities.User;

public interface AccountInterface extends EntityInterface {

	/*
	 * Retrive owner of account
	 */
	public User getOwner();
	/*
	 * Set owner of account
	 */
	public void setOwner(User owner);
	/*
	 * Retrieve balance of account
	 */
	public double getBalance();
	/*
	 * Set balance of account
	 */
	public void setBalance(double balance);
}
