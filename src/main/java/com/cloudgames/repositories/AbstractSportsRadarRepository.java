package com.cloudgames.repositories;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.cloudgames.repositories.interfaces.SportsRadarEntityRepositoryInterface;

abstract public class AbstractSportsRadarRepository<T> extends AbstractHibernateRepository<T> implements SportsRadarEntityRepositoryInterface<T> {

	@Override
	abstract protected Criteria getCriteria();
	
	@SuppressWarnings("unchecked")
	public T fetchBySportsRadarId(String id) {
		Criteria criteria = this.getCriteria();
		
		criteria.add( Restrictions.eq("sportRadarId", id) );
		
		return (T)criteria.uniqueResult();		
	}
	
	

}
