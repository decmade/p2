package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

public interface CustomHttpExceptionInterface
{
	/**
	 * returns a message customized for the request passed
	 */
	public String getRequestMessage(HttpServletRequest request);

	HttpStatus getStatus();
	
}
