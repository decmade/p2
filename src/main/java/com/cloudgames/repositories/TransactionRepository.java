package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.Transaction;
import com.cloudgames.entities.interfaces.TransactionInterface;
import com.cloudgames.repositories.interfaces.TransactionRepositoryInterface;

@Repository("transaction-repository")
public class TransactionRepository extends AbstractHibernateRepository<TransactionInterface> implements TransactionRepositoryInterface {

	@Override
	public TransactionInterface fetchByAccount(Account acc) {
		Criteria criteria = this.getCriteria();
		
		log.debug("retrieving user with account ID: " + acc.getId() + " from persistent storage");
		criteria.add(Restrictions.eq("accountID", acc.getId()) );
		
		return (TransactionInterface)criteria.uniqueResult();
	}
	
	@Override
	public List<TransactionInterface> fetchAll() {
		log.debug("retrieving all transactions from persistent storage");
		
		return super.fetchAll();
	}

	
	@Override
	public TransactionInterface fetchById(int id) {
		String message = String.format("retrieving user with ID[%d] from persistent storage", id);
		
		log.debug(message);
		
		return super.fetchById(id);
	}
	
	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(Transaction.class);
	}

	

}
