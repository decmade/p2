package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

abstract public class AbstractCustomHttpException extends HTTPException implements CustomHttpExceptionInterface
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4268394077056754250L;

	public AbstractCustomHttpException(int statusCode) {
		super(statusCode);
	}

	@Override
	abstract public String getRequestMessage(HttpServletRequest request);
	
}
