package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.TransactionType;
import com.cloudgames.entities.interfaces.TransactionTypeInterface;
import com.cloudgames.repositories.interfaces.TransactionTypeRepositoryInterface;

@Repository("transactiontype-repository")
public class TransactionTypeRepository extends AbstractHibernateRepository<TransactionTypeInterface> implements TransactionTypeRepositoryInterface  {

	@Override
	public TransactionTypeInterface fetchById(int id) {
		log.debug("retrieving UserStatus with ID: " + id + " from persistent storage");
		
		return super.fetchById(id);
	}

	@Override
	public List<TransactionTypeInterface> fetchAll() {
		log.debug("retrieving all TransactionTypes from persistent storage");
		
		return super.fetchAll();
	}

	@Override
	public TransactionTypeInterface save(TransactionTypeInterface type) {	
		if ( type.getId() > 0 ) {
			log.debug("updating TransactionType with ID: "  + type.getId() + " in persistent storage");
		} else {
			log.debug("added new TransactionType to persistent storage");
		}
		
		return super.save(type);
	}

	@Override
	public void delete(TransactionTypeInterface type) {
		log.debug(("deleting TransactionType with ID: " + type.getId() ));
		
		super.delete(type);
	}
	
	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(TransactionType.class);
	}

}
