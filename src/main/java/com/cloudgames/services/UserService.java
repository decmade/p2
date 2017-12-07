package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.repositories.interfaces.RepositoryInterface;
import com.cloudgames.repositories.interfaces.UserRepositoryInterface;
import com.cloudgames.services.interfaces.UserServiceInterface;

@Service("user-service")
public class UserService extends AbstractService<UserInterface> implements UserServiceInterface {
	
	@Autowired
	@Qualifier("user-repository")
	private UserRepositoryInterface repository;
	
	@Override
	public UserInterface fetchByIdentity(String identity) {
		String message = String.format("retrieving user with IDENTITY[%s] from repository", identity);
		
		log.debug(message);
		
		return this.repository.fetchByIdentity(identity);
	}

	@Override
	@Transactional
	public UserInterface fetchById(int id) {
		String message = String.format("retrieving user with ID[%d] from repository", id);
		
		log.debug(message);
		
		return this.repository.fetchById(id);
	}

	@Override
	@Transactional
	public List<UserInterface> fetchAll() {
		String message = "retrieving all users from repository";
		
		log.debug(message);
		
		return this.repository.fetchAll();
	}

	@Override
	@Transactional
	public UserInterface save (UserInterface user) {
		String message = String.format("saving user with ID[%d] to respository", user.getId() );
		
		log.debug(message);
		this.repository.save(user);
		
		return user;
	}

	@Override
	@Transactional
	public void delete(UserInterface user) {
		String message = String.format("removing user with ID[%d] from the respository", user.getId() );
		
		log.debug(message);
		this.repository.delete(user);		
	}

	
}
