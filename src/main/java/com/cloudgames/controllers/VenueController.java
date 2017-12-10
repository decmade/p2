package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.Venue;
import com.cloudgames.entities.interfaces.VenueInterface;
import com.cloudgames.services.interfaces.VenueServiceInterface;

@RestController("venue-controller")
@RequestMapping("venues")
public class VenueController extends AbstractController<VenueInterface, Venue> {
	
	@Autowired
	@Qualifier("venue-service")
	private VenueServiceInterface service;

	@Override
	@GetMapping("{id:[0-9]+}")
	public VenueInterface get(@PathVariable int id) {
		String message = String.format("retreiving Venue with ID[%d]",  id);
		
		log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<VenueInterface> getAll() {
		String message = "retrieving all Venues";
		
		log.debug(message);
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public VenueInterface save(@RequestBody Venue venue) {
		String message = "";
		
		if ( venue.getId() > 0 ) {
			message = String.format("saving Venue with ID[%d]", venue.getId() );
		} else {
			message = "adding venue";
		}
		
		log.debug(message);
		
		return this.service.save(venue);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody Venue venue) {
		String message = String.format("removing Venue with ID[%d]", venue.getId() );
		
		log.debug(message);
		
		this.service.delete(venue);
		
	}

}
