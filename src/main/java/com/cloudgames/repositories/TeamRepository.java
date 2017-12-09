package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.Team;
import com.cloudgames.entities.interfaces.TeamInterface;
import com.cloudgames.repositories.interfaces.TeamRepositoryInterface;

@Repository("team-repository")
public class TeamRepository extends AbstractHibernateRepository<TeamInterface> implements TeamRepositoryInterface {

	@Override
	public TeamInterface fetchById(int id) {
		String message = String.format("retrieving Team with ID[%d] from persistent storage", id);
		
		this.log.debug(message);
		
		return super.fetchById(id);
	}

	@Override
	public List<TeamInterface> fetchAll() {
		String message = "retrieving all Teams from persistent storage";
		
		this.log.debug(message);
		
		return super.fetchAll();
	}

	@Override
	public TeamInterface save(TeamInterface team) {
		String message = "";
		int id = team.getId();
		
		if ( id > 0 ) {
			message = String.format("updating Team with ID[%d] in persistent storage", id);
		} else {
			message = "saving new Team to persistent storage";
		}
		
		this.log.debug(message);
		
		return super.save(team);
	}

	@Override
	public void delete(TeamInterface team) {
		String message = String.format("removing Team from persistent storage with ID[%d]", team.getId() );
		
		this.log.debug(message);
		
		super.delete(team);
	}

	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(Team.class);
	}
}
