package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.Sport;
import com.cloudgames.entities.interfaces.SportInterface;
import com.cloudgames.repositories.interfaces.SportRepositoryInterface;

@Repository("sport-repository")
public class SportRepository extends AbstractHibernateRepository<SportInterface> implements SportRepositoryInterface {

	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(Sport.class);
	}
	
	

	@Override
	public SportInterface fetchById(int id) {
		String message = String.format("retrieving Sport with ID[%d] from persistent storage", id);
		
		this.log.debug(message);
		
		return super.fetchById(id);
	}

	@Override
	public List<SportInterface> fetchAll() {
		String message = "retrieving all Sports from persistent storage";
		
		this.log.debug(message);
		
		return super.fetchAll();
	}

	@Override
	public SportInterface save(SportInterface sport) {
		String message = "";
		int id = sport.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Sport with ID[%d] in persistent storage", id);
		} else {
			message = "saving new Sport to persistent storage";
		}
		
		this.log.debug(message);
		
		return super.save(sport);
	}

	@Override
	public void delete(SportInterface sport) {
		String message = String.format("deleting Sport with ID[%d] from persistent storage", sport.getId() );
		
		this.log.debug(message);
		
		super.delete(sport);
	}
	
}
