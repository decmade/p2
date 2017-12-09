package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;

import com.cloudgames.acl.Authenticator;
import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.http.HttpServletRequestUtil;

public class UnauthorizedHttpRequestException extends AbstractCustomHttpException
{

	@Autowired
	@Qualifier("authenticator")
	private Authenticator authenticator;
	
	@Autowired
	@Qualifier("utility-http-request")
	private HttpServletRequestUtil requestUtil;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2647774923845886771L;

	public UnauthorizedHttpRequestException() 
	{
		super( HttpStatus.UNAUTHORIZED );		
	}

	/**
	 * returns a message customized for the request passed
	 */
	@Override
	public String getRequestMessage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Authenticator auth = this.getAuthenticator();
		UserInterface user = null;

		
		auth.setSession(session);
		user = auth.getAuthenticatedUser();
		
		if ( user == null ) {
			user = new User();
			user.setIdentity("anonymous");			
		}
		
		String message = String.format("User:[%s] does not have permission for [%s] request to [%s]",
				user.getIdentity(),
				request.getMethod(),
				this.requestUtil.extractRelativeUri(request)
		);
		
		return message;
	}

	private Authenticator getAuthenticator() {
		return this.authenticator;
	}
}
