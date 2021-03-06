package com.cloudgames.repositories;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.cloudgames.repositories.interfaces.SportsRadarEntityRepositoryInterface;

abstract public class AbstractSportsRadarRepository<T> extends AbstractHibernateRepository<T> implements SportsRadarEntityRepositoryInterface<T> {

	@Override
	abstract protected Criteria getCriteria();
	
	@Override
	@SuppressWarnings("unchecked")
	public T fetchBySportsRadarId(String id) {
		Criteria criteria = this.getCriteria();
		
		criteria.add( Restrictions.eq("sportsRadarId", id) );
		
		return (T)criteria.uniqueResult();		
	}
	
	

}
