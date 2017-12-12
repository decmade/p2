package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.UserRoleInterface;
import com.cloudgames.repositories.interfaces.UserRoleRepositoryInterface;
import com.cloudgames.services.interfaces.UserRoleServiceInterface;

@Service("userrole-service")
public class UserRoleService extends AbstractService<UserRoleInterface> implements UserRoleServiceInterface {

	@Autowired
	@Qualifier("userrole-repository")
	protected UserRoleRepositoryInterface repository;
	
	@Override
	@Transactional
	public UserRoleInterface fetchById(int id) {
		String message = String.format("retrieving UserRole with ID[%d] from repository", id);
		
		log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	@Transactional
	public List<UserRoleInterface> fetchAll() {
		String message = "retrieving all UserRole objects from repository";
				
		log.debug(message);
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserRoleInterface save(UserRoleInterface role) {
		String message = "";
		
		if ( role.getId() > 0 ) {
			message = String.format("updating UserRole with ID[%d] in the repository", role.getId() );
		} else {
			message = "adding new UserRole object to the repository";
		}
		
		log.debug(message);
		
		return this.repository.save(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UserRoleInterface role) {
		String message = String.format("deleting UserRole with ID[%d] from repository", role.getId() );
		
		log.debug(message);
		
		this.repository.delete(role);
		
	}

}
