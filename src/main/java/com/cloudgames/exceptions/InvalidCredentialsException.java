package com.cloudgames.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AbstractCustomHttpException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1937399379871645884L;

	public InvalidCredentialsException() {
		super(HttpStatus.FORBIDDEN);
	}
	
	@Override
	public String getRequestMessage(HttpServletRequest request) {
		return "invalid credentials given";
	}

}
