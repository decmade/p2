package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.cloudgames.entities.interfaces.VenueInterface;
import com.cloudgames.repositories.interfaces.VenueRepositoryInterface;
import com.cloudgames.services.interfaces.VenueServiceInterface;

@Service("venue-service")
public class VenueService extends AbstractService<VenueInterface> implements VenueServiceInterface {

	@Autowired
	@Qualifier("venue-repository")
	private VenueRepositoryInterface repository;
	
	@Override
	public VenueInterface fetchById(int id) {
		String message = String.format("retrieving Venue with ID[%d] from repository", id);
		
		log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	public List<VenueInterface> fetchAll() {
		String message = "retrieving all Venues from respository";
		
		log.debug(message);
		return this.repository.fetchAll();
	}

	@Override
	public VenueInterface save(VenueInterface venue) {
		String message = "";
		
		if ( venue.getId() > 0 ) {
			message = String.format("updating Venue with ID[%d] in respository", venue.getId() );
		} else {
			message = "adding new Venue to repository";
		}
		
		log.debug(message);
		
		return this.repository.save(venue);
	}

	@Override
	public void delete(VenueInterface venue) {
		String message = String.format("deleting Venue with ID[%d] in repository",  venue.getId() );
		
		log.debug(message);
		
		this.repository.delete(venue);		
	}
	

}