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

	@Column(nullable = false)
	private double amount;
	
	private String created;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transactionType_id")
	private TransactionType type;
	
	@Override
	public double getAmount() {
		return amount;
	}
	
	@Override
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public TransactionType getType() {
		return type;
	}
	
	@Override
	public void setType(TransactionType transactionType) {
		this.type = transactionType;
	}
	
	@Override
	public String getCreated() {
		return created;
	}
	
	@Override
	public void setCreated(String created) {
		this.created = created;
	}
	
}
	
	
	
