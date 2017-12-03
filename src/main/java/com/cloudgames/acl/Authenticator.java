package com.cloudgames.acl;

import javax.servlet.http.HttpSession;

import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.entities.wrappers.UserWrapper;
import com.cloudgames.dbal.EntityManager;
import com.cloudgames.logger.AclLogger;
import com.cloudgames.logger.LoggerInterface;

/**
 * << utility >>
 * 
 * represents the logic for authenticating a
 * valid user
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class Authenticator {
	private static LoggerInterface log = AclLogger.getInstance();		// get handle for LoggerInterface
	public static final String USER_SESSION_KEY = "user";				// key used to reference the user stored in the Session
		
	/**
	 * authenticate a user with the passed identity and password
	 * 
	 * @param HttpSession session
	 * @param String identity
	 * @param String password
	 * 
	 * @return boolean
	 */
	public static boolean authenticate(HttpSession session, String identity, String password)
	{
		UserInterface user = null;
		UserWrapper userWrapper = new UserWrapper();
		String logMessage;
		
		clear(session);
		user = EntityManager.getUserByIdentity(identity);
		userWrapper.setSubject( user );
		
		/*
		 * if there is no user found with that identity
		 * then the authentication fails
		 */
		if ( userWrapper.hasSubject() == false ) {
			logMessage = String.format("LOGIN FAILED:: user with identity [%s] was not found", identity);
			log.info( logMessage );
			return false;
		}
		
		/*
		 * if the user's password hash matches the one
		 * in the database then it is valid
		 */

		if ( userWrapper.checkCredential( password ) ) {
			logMessage = String.format("LOGIN SUCCESSFULL:: user %s logged in successfully", userWrapper.getIdentity() );
			log.info( logMessage );
			
			/*
			 * store user in session
			 */
			session.setAttribute(USER_SESSION_KEY, String.valueOf( userWrapper.getId() ) );

			return true;
		} 
		
		/*
		 * default policy
		 */
		logMessage = String.format("LOGIN FAILED:: login attempt for user %s failed with bad credentials", userWrapper.getIdentity() );
		log.info( logMessage );
		return false;
	}
	
	/**
	 * retrieves the currently authenticated user
	 * from the current session
	 * 
	 * @param HttpSession session
	 * 
	 * @return User|null
	 */
	public static UserInterface getAuthenticatedUser(HttpSession session)
	{
		String id = (String)session.getAttribute( USER_SESSION_KEY);
		UserInterface user = null;
		String message = "";
		
		message = String.format("attempting to retrieve authenticated user from session with ID:[%s]", id );
		log.debug(message);
		
		if ( id == null || id.isEmpty() || id.equals("0") ) {
			message = String.format("could not retrieve user from database with ID:[%s]", id );
			log.debug( message );
		} else {
			user = EntityManager.getUser( id );
		}
		
		
		return user;
	}
	
	/**
	 * remove authenticated user from session
	 * 
	 * @param HttpSession session
	 */
	public static void clear(HttpSession session)
	{
		String message = "";
		
		message = String.format( "LOGOUT: user with ID:[%s] logged out of authenticated session", session.getAttribute(USER_SESSION_KEY) );
		log.info( message );
		session.setAttribute(USER_SESSION_KEY, "");
		
	}

}
