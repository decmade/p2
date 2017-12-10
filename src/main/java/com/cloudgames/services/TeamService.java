package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.TeamInterface;
import com.cloudgames.repositories.interfaces.TeamRepositoryInterface;
import com.cloudgames.services.interfaces.TeamServiceInterface;

@Service("team-service")
public class TeamService extends AbstractService<TeamInterface> implements TeamServiceInterface {

	@Autowired
	@Qualifier("team-repository")
	private TeamRepositoryInterface repository;

	@Override
	public TeamInterface fetchById(int id) {
		String message = String.format("retrieving Team with ID[%d] from repository", id);
		
		this.log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	public List<TeamInterface> fetchAll() {
		String message = "retrieving all Teams from respository";
		
		this.log.debug(message);
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TeamInterface save(TeamInterface team) {
		String message = "";
		String srId = team.getSportsRadarId();
		TeamInterface previous = this.repository.fetchBySportsRadarId(srId);
		int id = team.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Team with ID[%d] in respository", id);
		} else {
			if ( previous == null ) {
				message = "adding new Team to repository";
			} else {
				message = String.format("updating Team with SportsRadarID[%s] in respository", srId);
				team.setId( previous.getId() );
			}
			
		}
		
		this.log.debug(message);
		
		return this.repository.save(team);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(TeamInterface team) {
		String message = String.format("deleting Team with ID[%d] from repository", team.getId() );
		
		this.log.debug(message);
		
		this.repository.delete(team);		
	}

		
}
