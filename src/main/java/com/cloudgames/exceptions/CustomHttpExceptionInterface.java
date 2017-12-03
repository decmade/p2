package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;

public interface CustomHttpExceptionInterface
{
	/**
	 * returns a message customized for the request passed
	 */
	public String getRequestMessage(HttpServletRequest request);
	
}
