package com.cloudgames.repositories;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.repositories.interfaces.UserRepositoryInterface;


@Repository("user-repository")
public class UserRepository extends AbstractHibernateRepository<UserInterface> implements UserRepositoryInterface {

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
		
		if ( user.getId() > 0 ) {
			message = String.format("updating user with ID[%d] in persistent storage", ((User)user).getId() );
		} else {
			message = "saving new user to persistent storage";
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
