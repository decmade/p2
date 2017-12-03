package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;

import com.cloudgames.acl.Acl;
import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.entities.wrappers.UserWrapper;
import com.cloudgames.http.HttpServletRequestUtil;

public class UnauthorizedHttpRequestException extends AbstractCustomHttpException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2647774923845886771L;

	public UnauthorizedHttpRequestException(int statusCode) 
	{
		super(statusCode);		
	}

	/**
	 * returns a message customized for the request passed
	 */
	@Override
	public String getRequestMessage(HttpServletRequest request) {
		UserInterface user = Acl.getAuthenticatedUser( request.getSession() );
		UserWrapper userWrapper = new UserWrapper();
		
		userWrapper.setSubject( user );
		
		if ( userWrapper.hasSubject() == false ) {
			user = new User();
			userWrapper.setSubject( user );
			userWrapper.setIdentity("anonymous");			
		}
		
		String message = String.format("User:[%s] does not have permission for [%s] request to [%s]",
				userWrapper.getIdentity(),
				request.getMethod(),
				HttpServletRequestUtil.extractRelativeUri(request)
		);
		
		return message;
	}

}
