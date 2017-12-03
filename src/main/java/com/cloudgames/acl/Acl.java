package com.cloudgames.acl;

import javax.servlet.http.HttpSession;

import com.cloudgames.entities.interfaces.UserInterface;

/**
 * << facade >>
 * 
 * provides a facade for ACL functions
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class Acl 
{
	/*
	 * Authenticator facades
	 */
	public static boolean authenticate(HttpSession session, String identity, String credential) 
	{
		return Authenticator.authenticate(session, identity, credential);
	}
	
	public static void clear(HttpSession session)
	{
		Authenticator.clear( session );
	}
	
	public static UserInterface getAuthenticatedUser(HttpSession session)
	{
		return Authenticator.getAuthenticatedUser( session );
	}
	
	/*
	 * Authorizer facades
	 */
	public static boolean authorize(UserInterface user, String verb, Object resource)
	{
		Request request = new Request(user, verb, resource);
		return Authorizer.getInstance().authorize(request);
	}
}
