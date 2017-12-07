package com.cloudgames.repositories.interfaces;

import com.cloudgames.entities.interfaces.UserInterface;

/**
 * this is the interface for the unique api hooks
 * that are on the UserRepository
 * 
 * this is how we can build on top of the basic api provided
 * by the RepositoryInterface
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public interface UserRepositoryInterface extends RepositoryInterface<UserInterface> {

	public UserInterface fetchByIdentity(String identity);
}
