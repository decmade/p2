package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.Game;
import com.cloudgames.entities.interfaces.GameInterface;
import com.cloudgames.services.interfaces.GameServiceInterface;



@RestController("game-controller")
@RequestMapping("games")
public class GameController extends AbstractController<GameInterface, Game> {

	@Autowired
	@Qualifier("game-service")
	private GameServiceInterface service;

	@Override
	@GetMapping("{id:[0-9]+}")
	public GameInterface get(@PathVariable int id) {
		String message = String.format("retrieving Game with ID[%d]",  id);
		
		this.log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<GameInterface> getAll() {
		String message = "retrieving all Games";
		
		this.log.debug(message);
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public GameInterface save(@RequestBody Game game) {
		String message = "";
		int id = game.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Game with ID[%d]", id);
		} else {
			message = "saving new Game";
		}
		
		this.log.debug(message);
		
		return this.service.save(game);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody Game game) {
		String message = String.format("removing Game with ID[%d]", game.getId() );
		
		this.log.debug(message);
		
		this.service.delete(game);		
	}
	
	
}
	
