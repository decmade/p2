package com.cloudgames.entities;

import java.util.List;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.AccountInterface;

@Entity
@Table(name="account")
public class Account extends AbstractEntity implements AccountInterface {

	@Column(nullable = false, precision = 12, scale = 2)
	private double balance;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "account_id")
	private List<Transaction> transactions;
	
	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public List<Transaction> getTransactions() {
		return transactions;
	}

	@Override
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	
}
	
	
	
