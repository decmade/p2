package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.Game;
import com.cloudgames.entities.interfaces.GameInterface;
import com.cloudgames.repositories.interfaces.GameRepositoryInterface;

@Repository("game-repository")
public class GameRepository extends AbstractHibernateRepository<GameInterface> implements GameRepositoryInterface {



	@Override
	public GameInterface fetchById(int id) {
		String message = String.format("retrieving Game from persistent storage with ID[%d]", id);
		
		this.log.debug(message);
		
		
		return super.fetchById(id);
	}

	@Override
	public List<GameInterface> fetchAll() {
		String message = "retrieving all Games from persistent storage";
		
		this.log.debug(message);
		
		return super.fetchAll();
	}

	@Override
	public GameInterface save(GameInterface game) {
		String message = "";
		int id = game.getId();
		
		if ( id > 0  ) {
			message = String.format("updating Game with ID[%d] in persistent storage", id);
		} else {
			message = "saving new Game to persistent storage";
		}
		
		this.log.debug(message);
		
		return super.save(game);
	}

	@Override
	public void delete(GameInterface game) {
		String message = String.format("deleting Game with ID[%d] from persistent storage", game.getId() );
		
		this.log.debug(message);
		
		super.delete(game);
	}
	
	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(Game.class);
	}

}
