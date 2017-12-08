package com.cloudgames.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.acl.Authenticator;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.exceptions.InvalidCredentialsException;

@RestController
@RequestMapping("auth")
public class AuthenticationController extends AbstractBasicController {

	@Autowired
	@Qualifier("authenticator")
	private Authenticator authenticator;
	
	
	/**
	 * this is the get current user api call
	 * 
	 * @param HttpServletRequest request
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping
	public UserInterface getAuthenticatedUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Authenticator auth = this.getAuthenticator();
		
		auth.setSession(session);
		
		
		return auth.getAuthenticatedUser();
	}
	
	/**
	 * this is the login api call
	 * 
	 * @param HttpServletRequest request
	 * @param String identity
	 * @param String password
	 * 
	 * @return ResponseEntity
	 */
	@PostMapping("{identity, credential}")
	public UserInterface login(HttpServletRequest request, String identity, String credential) {
		HttpSession session = request.getSession();
		Authenticator auth = this.getAuthenticator();
		
		auth.setSession(session);
		
		if ( auth.authenticate(identity, credential) ) {
			return auth.getAuthenticatedUser();
		} else {
			throw new InvalidCredentialsException();
		}
	}
	
	/**
	 * logout
	 * 
	 * @param request
	 */
	@DeleteMapping
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Authenticator auth = this.getAuthenticator();
		
		auth.setSession(session);
		auth.clear();
	}
	
	
	private Authenticator getAuthenticator() {
		return this.authenticator;
	}


}
