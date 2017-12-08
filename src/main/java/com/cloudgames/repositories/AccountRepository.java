package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.AccountInterface;
import com.cloudgames.repositories.interfaces.AccountRepositoryInterface;

@Repository("account-repository")
public class AccountRepository extends AbstractHibernateRepository<AccountInterface> implements AccountRepositoryInterface {

	@Override
	public AccountInterface fetchById(int id) {
		Criteria criteria = this.getCriteria();
		
		log.debug("retrieving account with ID from persistent stoarge: " + id);
		
		return super.fetchById(id);
	}

	@Override
	public AccountInterface fetchByOwner(User u) {
		Criteria criteria = this.getCriteria();
 		
 		log.debug("retrieving account with owner: " + u.getIdentity());
 		criteria.add(Restrictions.eq("owner", u.getIdentity()) );
 		
 		return (AccountInterface)criteria.uniqueResult();
	}
	
	@Override
 	public List<AccountInterface> fetchAll() {
 		log.debug("retrieving all accounts from persistent storage");
 		
 		return super.fetchAll();
 	}
	
	@Override
	public AccountInterface save(AccountInterface acc) {
		if ( acc.getId() > 0 ) {
			log.debug("updating account with ID: " + ((Account)acc).getId() + " in persistent storage");
		} else {
			 log.debug("saving new account to persistent storage");
		}
			  				
		return super.save(acc);
	}

	@Override
 	public void delete(AccountInterface acc) {
 		log.debug("deleting user with ID: " +  ((User)user).getId() + " from persistent storage");
 		
 		super.delete(acc);	
 	}
	
	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(Account.class);
	}

	
	
}
