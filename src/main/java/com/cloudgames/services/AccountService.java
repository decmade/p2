package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.AccountInterface;
import com.cloudgames.repositories.interfaces.AccountRepositoryInterface;
import com.cloudgames.services.interfaces.AccountServiceInterface;

@Service("account-service")
public class AccountService extends AbstractService<AccountInterface> implements AccountServiceInterface {

	@Autowired
	@Qualifier("account-repository")
	private AccountRepositoryInterface repository;

	@Override
	@Transactional
	public AccountInterface fetchByOwner(User u) {
		log.debug("retrieving account with owner: " + u.getIdentity() + " from repository");
		return this.repository.fetchByOwner(u);
	}

	@Override
	@Transactional
	public AccountInterface fetchById(int id) {
		log.debug("retrieving account with ID: " + id + " from repository");
		
		return this.repository.fetchById(id);
	}

	@Override
	@Transactional
	public List<AccountInterface> fetchAll() {
		log.debug("retrieving all accounts from repository");
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AccountInterface save(AccountInterface acc) {
		log.debug("saving account wth ID: " + acc.getId() + " to repository");
		this.repository.save(acc);
		
		return acc;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(AccountInterface acc) {
		log.debug("removing accout with ID: " + acc.getId() + " from repository");
		this.repository.delete(acc);	
	}
	
}
