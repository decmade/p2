package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cloudgames.entities.Account;
import com.cloudgames.entities.interfaces.TransactionInterface;
import com.cloudgames.repositories.interfaces.TransactionRepositoryInterface;
import com.cloudgames.services.interfaces.TransactionServiceInterface;

@Service("transaction-service")
public class TransactionService extends AbstractService<TransactionInterface> implements TransactionServiceInterface {

	@Autowired
	@Qualifier("transaction-repository")
	private TransactionRepositoryInterface repository;
	
	@Override
	public TransactionInterface fetchByAccount(Account acc) {
		log.debug("retrieving transaction with account ID: " + acc.getId() + " from repository");
		return this.repository.fetchByAccount(acc);
	}
	
	@Override
	public TransactionInterface fetchById(int id) {
		log.debug("retrieving transaction with ID: " + id + " from repository");
		
		return this.repository.fetchById(id);
	}

	@Override
	public List<TransactionInterface> fetchAll() {
		log.debug("retrieving all transactions from repository");
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TransactionInterface save(TransactionInterface trans) {
		log.debug("saving transaction wth ID: " + trans.getId() + " to repository");
		this.repository.save(trans);
		
		return trans;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(TransactionInterface trans) {
		log.debug("removing transaction with ID: " + trans.getId() + " from repository");
		this.repository.delete(trans);
	}

	
	
}
