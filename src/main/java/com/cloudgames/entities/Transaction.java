package com.cloudgames.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudgames.entities.interfaces.TransactionInterface;

@Entity
@Table(name="transactions")
public class Transaction extends AbstractEntity implements TransactionInterface {

	@Column(unique = false, nullable = false, precision = 15, scale = 2)
	private double amount;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Account acc;
	@Column
	private static final int STATUS_PENDING = 1;
	@Column
	private static final int STATUS_COMPLETED = 2;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private TransactionType transactionType;
	
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Transaction(double amount, Account acc, TransactionType transactionType) {
		super();
		this.amount = amount;
		this.acc = acc;
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Account getAcc() {
		return acc;
	}
	public void setAcc(Account acc) {
		this.acc = acc;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	
}
	
	
	
