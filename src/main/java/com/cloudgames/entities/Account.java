package com.cloudgames.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudgames.entities.interfaces.AccountInterface;

@Entity
@Table(name="account")
public class Account extends AbstractEntity implements AccountInterface {

	@Column(unique = false, nullable = false, precision = 12, scale = 2)
	private double balance;
	
	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(double balance) {
		super();
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
}
	
	
	
