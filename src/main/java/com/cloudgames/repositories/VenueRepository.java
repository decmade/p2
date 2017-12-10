package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.Venue;
import com.cloudgames.entities.interfaces.VenueInterface;
import com.cloudgames.repositories.interfaces.VenueRepositoryInterface;

@Repository("venue-repository")
public class VenueRepository extends AbstractSportsRadarRepository<VenueInterface> implements VenueRepositoryInterface {



	@Override
	public VenueInterface fetchBySportsRadarId(String id) {
		String message = String.format("retriving Venue with SportsRadarID[%s]", id);
		
		this.log.debug(message);
		
		return super.fetchBySportsRadarId(id);
	}

	@Override
	public VenueInterface fetchById(int id) {
		String message = String.format("retrieving Venue with ID[%d] from persistent storage", id);
		
		this.log.debug(message);
		
		return super.fetchById(id);
	}

	@Override
	public List<VenueInterface> fetchAll() {
		String message = "retrieving all Venues from persistent storage";
		
		this.log.debug(message);
		
		return super.fetchAll();
	}

	@Override
	public VenueInterface save(VenueInterface venue) {
		String message = "";
		
		if ( venue.getId() > 0 ) {
			message = String.format("updating Venue with ID[%d] in persistent storage", venue.getId() );
		} else {
			message = "adding new Venue to persistent storage";
		}
		
		this.log.debug(message);
		
		return super.save(venue);
	}

	@Override
	public void delete(VenueInterface venue) {
		String message = String.format("deleting Venue with ID[%d]", venue.getId());
		
		this.log.debug(message);
		
		super.delete(venue);
	}

	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(Venue.class);
	}
	
}
