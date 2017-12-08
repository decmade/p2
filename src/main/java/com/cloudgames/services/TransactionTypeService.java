package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudgames.entities.interfaces.TransactionTypeInterface;
import com.cloudgames.repositories.interfaces.TransactionTypeRepositoryInterface;
import com.cloudgames.services.interfaces.TransactionTypeServiceInterface;

@Service("transactiontype-service")
public class TransactionTypeService extends AbstractService<TransactionTypeInterface> implements TransactionTypeServiceInterface {

	@Autowired
	@Qualifier("transactiontype-repository")
	private TransactionTypeRepositoryInterface repository;
	
	@Override
	public TransactionTypeInterface fetchById(int id) {
		log.debug("retrieving TransactionType with ID: " + id);
		
		return this.repository.fetchById(id);
	}

	@Override
	public List<TransactionTypeInterface> fetchAll() {		
		log.debug("retrieving all UserStatuses from repository");
		
		return this.repository.fetchAll();
	}

	@Override
	public TransactionTypeInterface save(TransactionTypeInterface type) {
		if ( type.getId() > 0 ) {
			log.debug("updating UserStatus with ID: " + type.getId() + " in repository");
		} else {
			log.debug("adding new TransactionType to repository");
		}
		
		return this.repository.save(type);
	}

	@Override
	public void delete(TransactionTypeInterface type) {
		log.debug("deleting UserStatus with ID: " + type.getId() + " from repository" );
		
		this.repository.delete(type);
	}

}
