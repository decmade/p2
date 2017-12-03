package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;

public class InvalidHttpRequestException extends AbstractCustomHttpException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2334157433203438334L;

	public InvalidHttpRequestException(int statusCode) 
	{
		super(statusCode);		
	}

	/**
	 * returns a message customized for the request passed
	 */
	@Override
	public String getRequestMessage(HttpServletRequest request) {
		String methodName = request.getMethod();
		StringBuilder sbMessage = new StringBuilder( String.format("Invalid request for %s method:", methodName ) );
		
		switch( methodName.toLowerCase() ) {
			case "post" :
				sbMessage.append("POST requests must have valid JSON data in the body of the request.");
				break;
			case "get" :
				sbMessage.append("GET requests must either have a valid ID parameter specified on the URL OR ");
				sbMessage.append("no URL parameters at all");
				break;
			case "put" :
				sbMessage.append("PUT request must have a valid ID parameter specified.");
				sbMessage.append(" PUT request  must have valid JSON data in the body of the request.");
				break;
			case "delete" :
				sbMessage.append("DELETE requests must have a valid ID parameter specified on the URL.");
				break;			
		}
		
		
		return String.valueOf( sbMessage);
	}
	
	

	

}
