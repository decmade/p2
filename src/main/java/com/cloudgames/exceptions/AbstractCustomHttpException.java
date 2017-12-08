package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

import org.springframework.http.HttpStatus;

abstract public class AbstractCustomHttpException extends HTTPException implements CustomHttpExceptionInterface
{
	protected HttpStatus status;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4268394077056754250L;

	public AbstractCustomHttpException(HttpStatus status) {
		super( status.value() );
		this.status = status;
	}

	@Override
	public HttpStatus getStatus() {
		return this.status;
	}
	
	@Override
	abstract public String getRequestMessage(HttpServletRequest request);
	
}
