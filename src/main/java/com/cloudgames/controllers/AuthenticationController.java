package com.cloudgames.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.acl.interfaces.AuthenticatorInterface;
import com.cloudgames.acl.models.Credentials;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.exceptions.InvalidCredentialsException;

@RestController("authentication-controller")
@RequestMapping("auth")
public class AuthenticationController extends AbstractBasicController {

	@Autowired
	@Qualifier("authenticator")
	private AuthenticatorInterface authenticator;
	
	
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
		AuthenticatorInterface auth = this.getAuthenticator();
		
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
	@PostMapping
	public UserInterface login(HttpServletRequest request, @RequestBody Credentials credentials) {
		HttpSession session = request.getSession();
		AuthenticatorInterface auth = this.getAuthenticator();
		
		auth.setSession(session);
		
		if ( auth.authenticate(credentials) ) {
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
		AuthenticatorInterface auth = this.getAuthenticator();
		
		auth.setSession(session);
		auth.clear();
	}
	
	
	private AuthenticatorInterface getAuthenticator() {
		return this.authenticator;
	}


}
