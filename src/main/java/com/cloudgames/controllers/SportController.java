package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.Sport;
import com.cloudgames.entities.interfaces.SportInterface;
import com.cloudgames.services.interfaces.SportServiceInterface;

@RestController("sport-controller")
@RequestMapping("sports")
public class SportController extends AbstractController<SportInterface, Sport>{
	
	@Autowired
	@Qualifier("sport-service")
	private SportServiceInterface service;

	@Override
	@GetMapping("{id:[0-9]+")
	public SportInterface get(@PathVariable int id) {
		String message = String.format("retrieving Sport with ID[%d]",  id);
		
		this.log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<SportInterface> getAll() {
		String message = "retrieving all Sports";
		
		this.log.debug(message);
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public SportInterface save(@RequestBody Sport sport) {
		String message = "";
		int id = sport.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Sport with ID[%d]", id);			
		} else {
			message = "saving new Sport";
		}
		
		this.log.debug(message);
		
		return this.service.save(sport);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody Sport sport) {
		String message = String.format("removing Sport with ID[%d]", sport.getId() );
		
		this.log.debug(message);
		
		this.service.delete(sport);		
	}
	
	
}
