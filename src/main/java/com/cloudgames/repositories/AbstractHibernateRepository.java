package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudgames.entities.interfaces.EntityInterface;

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
	public T fetchById(int id) {
		Criteria criteria = this.getCriteria();
	
		criteria.add(Restrictions.eq("id", id));
		
		return (T)criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> fetchAll() {
		Criteria criteria = this.getCriteria();
		
		return criteria.list();
	}

	@Override
	public T save(T entity) {		
		Session session = this.getSession();
		int id = ((EntityInterface)entity).getId();
		
		if ( id > 0 ) {
			this.log.debug("detected existing entity");
			session.merge(entity);
		} else {
			this.log.debug("detected transient entity");
			session.persist(entity);
			session.flush();
		}

		return entity;
	}

	@Override
	public void delete(T entity) {
		this.getSession().delete(entity);
	}

	
	/**
	 * returns a Hibernate Session instance
	 * 
	 * @return Session
	 */
	protected Session getSession() {
			Session session = null;
			
			try {
				this.log.debug("attempting to attain an existing Hibernate session");
				
				session = this.sessionFactory.getCurrentSession();
				
				this.log.debug("successfully attained existing Hibernate session");
				
			} catch(Exception e) {
//				this.log.debug("no existing Hibernate session found");
//				this.log.debug("opening new Hibernate session");
//				
//				session = this.sessionFactory.openSession();
				/*
				 * should never be opening new sessions. this created
				 * a resource leak
				 */
				this.log.error( e.getMessage() );
			}
		
		return session;
	}
	
}
