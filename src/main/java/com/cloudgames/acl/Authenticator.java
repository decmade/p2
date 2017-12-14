package com.cloudgames.acl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.cloudgames.acl.interfaces.AuthenticatorInterface;
import com.cloudgames.acl.models.Credentials;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.io.Encryption;
import com.cloudgames.services.interfaces.UserServiceInterface;

/**
 * << utility >>
 * 
 * represents the logic for authenticating a
 * valid user
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@Service("authenticator")
public class Authenticator extends AbstractAclObject implements AuthenticatorInterface {
	public static final String USER_SESSION_KEY = "user";				// key used to reference the user stored in the Session
	
	@Autowired
	@Qualifier("user-service")
	private UserServiceInterface userService;
	
	@Autowired
	@Qualifier("encryption")
	private Encryption encryption;
	
	private HttpSession session;
	
	@Override
	public void setSession(HttpSession session) {
		this.session = session;
	}
		
	/**
	 * authenticate a user with the passed identity and password
	 * 
	 * @param String identity
	 * @param String password
	 * 
	 * @return boolean
	 */
	@Override
	public boolean authenticate(Credentials credentials)
	{
		UserInterface user = this.userService.fetchByIdentity(credentials.identity);
		String logMessage;
		
		this.clear();
		
		/*
		 * if there is no user found with that identity
		 * then the authentication fails
		 */
		if ( user == null ) {
			logMessage = String.format("LOGIN FAILED:: user with identity [%s] was not found", credentials.identity);
			log.info( logMessage );
			return false;
		}
		
		/*
		 * if the user's password hash matches the one
		 * in the database then it is valid
		 */

		if ( this.checkCredentials(user, credentials.password) ) {
			logMessage = String.format("LOGIN SUCCESSFULL:: user %s logged in successfully", user.getIdentity() );
			log.info( logMessage );
			
			/*
			 * store user in session
			 */
			session.setAttribute(USER_SESSION_KEY, String.valueOf( user.getId() ) );

			return true;
		} 
		
		/*
		 * default policy
		 */
		logMessage = String.format("LOGIN FAILED:: login attempt for user %s failed with bad credentials", user.getIdentity() );
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
	@Override
	public UserInterface getAuthenticatedUser()
	{
		int id = 0;
		String idString = (String)this.session.getAttribute( USER_SESSION_KEY);
		UserInterface user = null;
		String message = "";
		
		message = String.format("attempting to retrieve authenticated user from session with ID:[%s]", idString );
		log.debug(message);
		
		try {
			
			if ( idString.isEmpty() ) {
				message = String.format("could not retrieve user  with ID:[%d]", id );
				log.debug( message );
			} else {
				id = Integer.parseInt( idString );
				user = this.userService.fetchById(id);
				
				if ( id > 0 ) {
					message = String.format("retrieved user with ID:[%d]", id);
					log.debug(message);
				}
			}
			
		} catch(Exception e) {
			log.error(e.getMessage() );
		}
		
		
		return user;
	}
	
	/**
	 * remove authenticated user from session
	 * 
	 * @param HttpSession session
	 */
	@Override
	public void clear()
	{
		String message = "";
		
		message = String.format( "LOGOUT: user with ID:[%s] logged out of authenticated session", this.session.getAttribute(USER_SESSION_KEY) );
		log.info( message );
		this.session.setAttribute(USER_SESSION_KEY, "");
		
	}
	
	/**
	 * return true if the user's encrypted password/credential matches
	 * the encrypted version of the attempt
	 * 
	 * @param UserInterface user
	 * @param String unencryptedAttempt
	 * 
	 * @return boolean
	 */
	private boolean checkCredentials(UserInterface user, String unencryptedAttempt ) {
		String secret = user.getSecret();
		String attempt = this.encryption.encrypt(unencryptedAttempt, secret);
		
		return attempt.equals( user.getCredential() );
	}

}
