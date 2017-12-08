package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.Venue;
import com.cloudgames.entities.interfaces.VenueInterface;
import com.cloudgames.services.interfaces.VenueServiceInterface;

@RestController
@RequestMapping("venues")
public class VenueController extends AbstractController<VenueInterface, Venue> {
	
	@Autowired
	@Qualifier("venue-service")
	private VenueServiceInterface service;

	@Override
	@GetMapping("{id:[0-9]+}")
	public VenueInterface get(@RequestBody int id) {
		String message = String.format("retreiving Venue with ID[%d]",  id);
		
		log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	public List<VenueInterface> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VenueInterface save(Venue entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Venue entity) {
		// TODO Auto-generated method stub
		
	}

}
