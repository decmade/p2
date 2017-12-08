package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.io.Encryption;
import com.cloudgames.repositories.interfaces.UserRepositoryInterface;


@Repository("user-repository")
public class UserRepository extends AbstractHibernateRepository<UserInterface> implements UserRepositoryInterface {

	@Autowired
	@Qualifier("encryption")
	private Encryption encryption;
	
	@Override
	public UserInterface fetchByIdentity(String identity) {
		String message = String.format("retrieving user with IDENTITY[%s] from persistent storage", identity);
		Criteria criteria = this.getCriteria();
		
		log.debug(message);
		criteria.add(Restrictions.eq("identity", identity) );
		
		return (UserInterface)criteria.uniqueResult();
	}
	
	@Override
	public UserInterface fetchById(int id) {
		String message = String.format("retrieving user with ID[%d] from persistent storage", id);
		
		log.debug(message);
		
		return super.fetchById(id);
	}

	@Override
	public List<UserInterface> fetchAll() {
		String message = "retrieving all users from persistent storage";
		
		log.debug(message);
		
		return super.fetchAll();
	}

	@Override
	public UserInterface save(UserInterface user) {
		String message = "";
		int id = user.getId();
		
		if ( id > 0 ) {
			message = String.format("updating user with ID[%d] in persistent storage", id );
			UserInterface previous = this.fetchById(id);
			String previousCredential = previous.getCredential();
			String currentCredential = user.getCredential();
			String secret = previous.getSecret();
			
			/*
			 * if the user has updated their password, then encrypt the
			 * value that is passed before saving to persistent storage
			 * using the secret key they had prior, and enforce that the secret
			 * key does not change
			 */
			if ( previousCredential.equals(currentCredential) == false ) {
				user.setCredential( this.encryption.encrypt(currentCredential, secret) );
				user.setSecret(secret);
			}
		} else {
			message = "saving new user to persistent storage";
			
			/*
			 * only trust secret keys we generate
			 * and encrypt the credential with it
			 */
			user.setSecret( this.encryption.generateKey() );
			user.setCredential( this.encryption.encrypt( user.getCredential(), user.getSecret() ) );
			
		}
		
		log.debug(message);
				
		return super.save(user);
	}

	@Override
	public void delete(UserInterface user) {
		String message = String.format("deleting user with ID[%d] from persistent storage", ((User)user).getId() );
		
		log.debug(message);
		
		super.delete(user);	
	}
	
	@Override
	protected Criteria getCriteria() {
		return this.getSession().createCriteria(User.class);
	}
	
}
