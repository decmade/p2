package com.cloudgames.acl;

import com.cloudgames.entities.interfaces.UserInterface;

/**
 * class for holding the data
 * of an access control request
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class Request {

	public Object resource;		// resource access is being requested to
	public UserInterface user;	// user requesting access
	public String verb;			// type/category of access
	
	/**
	 * factory constructor
	 * 
	 * @param UserInterface user
	 * @param String verb
	 * @param Object resource
	 */
	public Request(UserInterface user, String verb, Object resource)
	{
		this.user = user;
		this.verb = verb;
		this.resource = resource;
	}
	
}
