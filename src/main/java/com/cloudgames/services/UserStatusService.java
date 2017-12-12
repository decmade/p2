package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.UserStatusInterface;
import com.cloudgames.repositories.interfaces.UserStatusRepositoryInterface;
import com.cloudgames.services.interfaces.UserStatusServiceInterface;

@Service("userstatus-service")
public class UserStatusService extends AbstractService<UserStatusInterface> implements UserStatusServiceInterface {

	@Autowired
	@Qualifier("userstatus-repository")
	private UserStatusRepositoryInterface repository;
	
	@Override
	@Transactional
	public UserStatusInterface fetchById(int id) {
		String message = String.format("retrieving UserStatus with ID[%d] from repository", id);
		
		log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	@Transactional
	public List<UserStatusInterface> fetchAll() {
		String message = "retrieving all UserStatuses from repository";
		
		log.debug(message);
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserStatusInterface save(UserStatusInterface status) {
		String message = "";
		
		if ( status.getId() > 0 ) {
			message = String.format("updating UserStatus with ID[%d] in respository", status.getId() );
		} else {
			message = "adding new UserStatus to repository";
		}
		
		log.debug(message);
		
		return this.repository.save(status);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UserStatusInterface status) {
		String message = String.format("deleting UserStatus with ID[%d] from repository", status.getId() );
		
		log.debug(message);
		
		this.repository.delete(status);
	}

}
