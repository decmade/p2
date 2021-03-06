package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.UserRole;
import com.cloudgames.entities.UserStatus;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.io.Encryption;
import com.cloudgames.repositories.interfaces.UserRepositoryInterface;
import com.cloudgames.services.interfaces.UserRoleServiceInterface;
import com.cloudgames.services.interfaces.UserServiceInterface;
import com.cloudgames.services.interfaces.UserStatusServiceInterface;

@Service("user-service")
public class UserService extends AbstractService<UserInterface> implements UserServiceInterface {
	
	@Autowired
	@Qualifier("user-repository")
	private UserRepositoryInterface repository;
	
	@Autowired
	@Qualifier("encryption")
	private Encryption encryption;
	
	@Autowired
	@Qualifier("userstatus-service")
	private UserStatusServiceInterface userStatusService;
	
	@Autowired
	@Qualifier("userrole-service")
	private UserRoleServiceInterface userRoleService;
	
	@Override
	@Transactional
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
	@Transactional(propagation = Propagation.REQUIRED)
	public UserInterface save (UserInterface user) {
		String message = "";
		int id = user.getId();
		
		if ( id > 0 ) {
			message = String.format("updating User with ID[%d] in the repository", id);
			
			UserInterface previous = this.repository.fetchById(id);
			String previousCredential = previous.getCredential();
			String currentCredential = user.getCredential();
			String secret = previous.getSecret();
			
			/*
			 * if user has provided a value for the credential field
			 */
			if ( user.getCredential() != null ) {
			
				/*
				 * if the user has updated their password, then encrypt the
				 * value that is passed before saving to persistent storage
				 * using the secret key they had prior, and enforce that the secret
				 * key does not change
				 */
				if (previousCredential == null || previousCredential.equals(currentCredential) == false ) {
					if ( secret == null ) {
						secret = this.encryption.generateKey();
					}
					
					user.setCredential( this.encryption.encrypt(currentCredential, secret) );
					user.setSecret(secret);
				}
			} else {
				user.setCredential(previousCredential);
				user.setSecret(secret);
			}
		} else {
			message = "saving new User to the repository";			
			
			/*
			 * set default status
			 */
			if ( user.getStatus() == null ) {
				user.setStatus( (UserStatus)userStatusService.fetchById(1) );
			}
			
			if ( user.getRole() == null ) {
				user.setRole((UserRole)userRoleService.fetchById(1));
			}
			
			if ( user.getCredential() != null && user.getCredential().isEmpty() == false ) {
				/*
				 * only trust secret keys we generate
				 * and encrypt the credential with it
				 */
				user.setSecret( this.encryption.generateKey() );
				
				user.setCredential( this.encryption.encrypt( user.getCredential(), user.getSecret() ) );
			}
		}
		
		log.debug(message);
		this.repository.save(user);
		
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UserInterface user) {
		String message = String.format("removing user with ID[%d] from the respository", user.getId() );
		
		log.debug(message);
		this.repository.delete(user);		
	}

	
}
