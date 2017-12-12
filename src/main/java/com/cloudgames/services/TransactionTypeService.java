package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.TransactionTypeInterface;
import com.cloudgames.repositories.interfaces.TransactionTypeRepositoryInterface;
import com.cloudgames.services.interfaces.TransactionTypeServiceInterface;

@Service("transactiontype-service")
public class TransactionTypeService extends AbstractService<TransactionTypeInterface> implements TransactionTypeServiceInterface {

	@Autowired
	@Qualifier("transactiontype-repository")
	private TransactionTypeRepositoryInterface repository;
	
	@Override
	@Transactional
	public TransactionTypeInterface fetchById(int id) {
		log.debug("retrieving TransactionType with ID: " + id);
		
		return this.repository.fetchById(id);
	}

	@Override
	@Transactional
	public List<TransactionTypeInterface> fetchAll() {		
		log.debug("retrieving all TransactionType from repository");
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TransactionTypeInterface save(TransactionTypeInterface type) {
		if ( type.getId() > 0 ) {
			log.debug("updating TransactionType with ID: " + type.getId() + " in repository");
		} else {
			log.debug("adding new TransactionType to repository");
		}
		
		return this.repository.save(type);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(TransactionTypeInterface type) {
		log.debug("deleting TransactionType with ID: " + type.getId() + " from repository" );
		
		this.repository.delete(type);
	}

}
