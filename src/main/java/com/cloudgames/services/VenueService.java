package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.VenueInterface;
import com.cloudgames.repositories.interfaces.VenueRepositoryInterface;
import com.cloudgames.services.interfaces.VenueServiceInterface;

@Service("venue-service")
public class VenueService extends AbstractSportsRadarEntityService<VenueInterface> implements VenueServiceInterface {

	@Autowired
	@Qualifier("venue-repository")
	private VenueRepositoryInterface repository;
	
	
	
	@Override
	public VenueInterface fetchBySportsRadarId(String id) {
		String message = String.format("retrieving Venue with SportsRadarID[%s] from repository", id);
		
		this.log.debug(message);
		
		return this.repository.fetchBySportsRadarId(id);
	}

	@Override
	public VenueInterface fetchById(int id) {
		String message = String.format("retrieving Venue with ID[%d] from repository", id);
		
		this.log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	public List<VenueInterface> fetchAll() {
		String message = "retrieving all Venues from respository";
		
		this.log.debug(message);
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public VenueInterface save(VenueInterface venue) {
		String message = "";
		String srId = venue.getSportsRadarId();
		VenueInterface previous = this.repository.fetchBySportsRadarId(srId);
		
		if ( venue.getId() > 0 ) {
			message = String.format("updating Venue with ID[%d] in respository", venue.getId() );
		} else {
			if ( previous == null ) {
				message = "adding new Venue to repository";
			} else {
				message = String.format("updating Venue with SportsRadarID[%s]", srId);
				venue.setId( previous.getId() );
			}
		}
		
		this.log.debug(message);
		
		return this.repository.save(venue);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(VenueInterface venue) {
		String message = String.format("deleting Venue with ID[%d] in repository",  venue.getId() );
		
		this.log.debug(message);
		
		this.repository.delete(venue);		
	}
	

}
