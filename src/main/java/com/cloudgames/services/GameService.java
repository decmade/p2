package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.GameInterface;
import com.cloudgames.repositories.interfaces.GameRepositoryInterface;
import com.cloudgames.services.interfaces.GameServiceInterface;

@Service("game-service")
public class GameService extends AbstractService<GameInterface> implements GameServiceInterface {

	@Autowired
	@Qualifier("game-repository")
	private GameRepositoryInterface repository;

	@Override
	@Transactional
	public GameInterface fetchById(int id) {
		String message = String.format("retrieving Game with ID[%d] from repository", id);
		
		this.log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	@Transactional
	public List<GameInterface> fetchAll() {
		String message = "retrieving all Games from repository";
		
		this.log.debug(message);
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GameInterface save(GameInterface game) {
		String message = "";
		String srId = game.getSportsRadarId();
		GameInterface previous = this.repository.fetchBySportsRadarId( srId );
		int id = game.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Game with ID[%d] in repository", id);
		} else {
			if ( previous == null ) {
				message = "adding new Game to repository";
			} else {
				message = String.format("updating Game with SportRadarID[%s]", srId );
				game.setId( previous.getId() );
			}
			
		}
		
		this.log.debug(message);
		
		return this.repository.save(game);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(GameInterface game) {
		String message = String.format("deleting Game with ID[%d] from repository", game.getId() );
		
		this.log.debug(message);
		
		this.repository.delete(game);		
	}
	
	
}
