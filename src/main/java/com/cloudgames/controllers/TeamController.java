package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.Team;
import com.cloudgames.entities.interfaces.TeamInterface;
import com.cloudgames.services.interfaces.TeamServiceInterface;

@RestController("team-controller")
@RequestMapping("teams")
public class TeamController extends AbstractController<TeamInterface, Team> {
	
	@Autowired
	@Qualifier("team-service")
	private TeamServiceInterface service;

	@Override
	@GetMapping("{id:[0-9]+}")
	public TeamInterface get(@PathVariable int id) {
		String message = String.format("retrieving Team with ID[%d]", id);
		
		this.log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<TeamInterface> getAll() {
		String message = "retrieving all Teams";
		
		this.log.debug(message);
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public TeamInterface save(@RequestBody Team team) {
		String message = "";
		int id = team.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Team with ID[%d]", id);
		} else {
			message = "saving new Team";
		}
		
		this.log.debug(message);
		
		return this.service.save(team);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody Team team) {
		String message = String.format("removing Team with ID[%d]", team.getId() );
		
		this.log.debug(message);
		
		this.service.delete(team);
	}
	
	
}
