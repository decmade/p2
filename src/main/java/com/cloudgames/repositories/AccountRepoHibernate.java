package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.User;

public class AccountRepoHibernate implements AccountRepo {

	@Autowired
	private SessionFactory sf;

	@Override
	@Transactional
	public Account save(Account acc) {
		sf.getCurrentSession().save(acc);
		return acc;
	}

	@Override
	@Transactional
	public Account update(Account acc) {
		sf.getCurrentSession().update(acc);
		return acc;
	}

	@Override
	@Transactional
	public List<Account> findAll() {
		return sf.getCurrentSession().createCriteria(Account.class).list();
	}

	@Override
	@Transactional
	public Account findByOwner(User u) {
		Session session = sf.getCurrentSession();
		Criteria cr = session.createCriteria(Account.class);
		cr.add(Restrictions.eq("owner", u));
		return (Account) cr.uniqueResult();
	}
	
	
}
