package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.SportInterface;
import com.cloudgames.repositories.interfaces.SportRepositoryInterface;
import com.cloudgames.services.interfaces.SportServiceInterface;

@Service("sport-service")
public class SportService extends AbstractService<SportInterface> implements SportServiceInterface {

	@Autowired
	@Qualifier("sport-repository")
	private SportRepositoryInterface repository;

	@Override
	public SportInterface fetchById(int id) {
		String message = String.format("retrieving Sport with ID[%d] from respository", id);
		
		this.log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	public List<SportInterface> fetchAll() {
		String message = "retrieving all Sports from repository";
		
		this.log.debug(message);
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SportInterface save(SportInterface sport) {
		String message = "";
		int id = sport.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Sport with ID[%d] in the repository", id);
		} else {
			message = "saving new Sport to repository";
		}
		
		this.log.debug(message);
		
		return this.repository.save(sport);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(SportInterface sport) {
		String message = String.format("deleting Sport with ID[%d] from repository", sport.getId() );
		
		this.log.debug(message);
		
		this.repository.delete(sport);
	}
	
	
}
