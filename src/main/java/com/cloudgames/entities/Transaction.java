package com.cloudgames.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudgames.entities.interfaces.TransactionInterface;

@Entity
@Table(name="transactions")
public class Transaction extends AbstractEntity implements TransactionInterface {

	@Column(unique = false, nullable = false, precision = 15, scale = 2)
	private double amount;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="account_id")
	private Account acc;
	@Column
	private static final int STATUS_PENDING = 1;
	@Column
	private static final int STATUS_COMPLETED = 2;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transactionType_id")
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
	@Override
	public double getAmount() {
		return amount;
	}
	@Override
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public Account getAcc() {
		return acc;
	}
	@Override
	public void setAcc(Account acc) {
		this.acc = acc;
	}
	@Override
	public TransactionType getTransactionType() {
		return transactionType;
	}
	@Override
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	
}
	
	
	
