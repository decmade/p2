package com.cloudgames.repositories;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.interfaces.TransactionTypeInterface;
import com.cloudgames.repositories.interfaces.TransactionTypeRepositoryInterface;

@Repository("transactiontype-repository")
public class TransactionTypeRepository extends AbstractHibernateRepository<TransactionTypeInterface> implements TransactionTypeRepositoryInterface  {

	@Override
	protected Criteria getCriteria() {
		// TODO Auto-generated method stub
		return null;
	}

}
