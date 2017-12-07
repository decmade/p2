package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.UserRole;
import com.cloudgames.entities.interfaces.UserRoleInterface;
import com.cloudgames.repositories.interfaces.UserRoleRepositoryInterface;

@Repository("userrole-repository")
public class UserRoleRespository extends AbstractHibernateRepository<UserRoleInterface> implements UserRoleRepositoryInterface {


	@Override
	public UserRoleInterface fetchById(int id) {
		String message = String.format("retrieving UserRole with ID[%d] from persistent storage", id);
		
		log.debug(message);
		
		return super.fetchById(id);
	}

	@Override
	public List<UserRoleInterface> fetchAll() {
		String message = "retrieving all UserRole objects from persistent storage";
		
		log.debug(message);
		
		return super.fetchAll();
	}

	@Override
	public UserRoleInterface save(UserRoleInterface role) {
		String message = "";
		
		if ( role.getId() > 0 ) {
			message = String.format("updating user with ID[%d] in persistent storage", ((UserRole)role).getId() );
		} else {
			message = "saving new user to persistent storage";
		}
		
		log.debug(message);
		
		return super.save(role);
	}

	@Override
	public void delete(UserRoleInterface role) {
		String message = String.format("deleting UserRole with ID[%d] from persistent storage", ((UserRole)role).getId() );
		
		log.debug(message);
		
		super.delete(role);
	}

	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(UserRole.class);
	}
}
