package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.UserStatus;
import com.cloudgames.entities.interfaces.UserStatusInterface;
import com.cloudgames.repositories.interfaces.UserStatusRepositoryInterface;

@Repository("userstatus-repository")
public class UserStatusRepository extends AbstractHibernateRepository<UserStatusInterface> implements UserStatusRepositoryInterface {



	@Override
	public UserStatusInterface fetchById(int id) {
		String message = String.format("retrieving UserStatus with ID[%d] from persistent storage", id);
		
		log.debug(message);
		
		return super.fetchById(id);
	}

	@Override
	public List<UserStatusInterface> fetchAll() {
		String message = "retrieving all UserStatuses from persistent storage";
		
		log.debug(message);
		
		return super.fetchAll();
	}

	@Override
	public UserStatusInterface save(UserStatusInterface status) {
		String message = "";
		
		if ( status.getId() > 0 ) {
			message = String.format("updating UserStatus with ID[%d] in persistent storage", status.getId() );
		} else {
			message = "added new UserStatus to persistent storage";
		}
		
		log.debug(message);
		
		return super.save(status);
	}

	@Override
	public void delete(UserStatusInterface status) {
		String message = String.format("deleting UserStatus with ID[%d]", status.getId() );
		
		log.debug(message);
		
		super.delete(status);
	}

	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(UserStatus.class);
	}
}
