package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.*;

abstract public class AbstractHibernateRepository<T> extends AbstractRepository<T> {
	@Autowired
	protected SessionFactory sessionFactory;
	
	/**
	 * returns a Hibernate Criteria instance
	 * to be used within the class
	 */
	abstract protected Criteria getCriteria();

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public T fetchById(int id) {
		Criteria criteria = this.getCriteria();
		
		criteria.add(Restrictions.eq("id", id));
		
		return (T)criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<T> fetchAll() {
		Criteria criteria = this.getCriteria();
		return criteria.list();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T save(T entity) {
		this.getSession().persist(entity);
		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(T entity) {
		this.getSession().delete(entity);	
	}

	
	/**
	 * returns a Hibernate Session instance
	 * 
	 * @return Session
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

}
