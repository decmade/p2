package com.cloudgames.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cloudgames.entities.interfaces.AccountInterface;

@Entity
@Table(name="account")
public class Account extends AbstractEntity implements AccountInterface {

	@Column(unique = false, nullable = false, precision = 12, scale = 2)
	private double balance;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User owner;
	
	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(double balance, User owner) {
		super();
		this.balance = balance;
		this.owner = owner;
	}

	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public User getOwner() {
		return owner;
	}

	@Override
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
}
	
	
	
