package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.Transaction;
import com.cloudgames.entities.TransactionType;

public class TransactionRepoHibernate implements TransactionRepo {

	@Autowired
	private SessionFactory sf;
	
	@Override
	@Transactional
	public Transaction save(Transaction t) {
		sf.getCurrentSession().save(t);
		return t;
	}

	@Override
	@Transactional
	public Transaction update(Transaction t) {
		sf.getCurrentSession().update(t);
		return t;
	}

	@Override
	@Transactional
	public List<Transaction> findAll() {
		return sf.getCurrentSession().createCriteria(Transaction.class).list();
	}

	@Override
	@Transactional
	public Transaction findType(TransactionType ttype) {
		Session session = sf.getCurrentSession();
		Criteria cr = session.createCriteria(Transaction.class);
		cr.add(Restrictions.eq("type", ttype));
		return (Transaction) cr.uniqueResult();
	}

	@Override
	@Transactional
	public Transaction findAllPending(int status) {
		Session session = sf.getCurrentSession();
		Criteria cr = session.createCriteria(Transaction.class);
		cr.add(Restrictions.eq("Pending", status));
		return (Transaction) cr.uniqueResult();
	}

	@Override
	@Transactional
	public Transaction findAllCompleted(int status) {
		Session session = sf.getCurrentSession();
		Criteria cr = session.createCriteria(Transaction.class);
		cr.add(Restrictions.eq("Completed", status));
		return (Transaction) cr.uniqueResult();
	}

	@Override
	@Transactional
	public Transaction findAccount(Account acc) {
		Session session = sf.getCurrentSession();
		Criteria cr = session.createCriteria(Transaction.class);
		cr.add(Restrictions.eq("account", acc));
		return (Transaction) cr.uniqueResult();
	}

}
