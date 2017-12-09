package com.cloudgames.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cloudgames.exceptions.AbstractCustomHttpException;
import com.cloudgames.logger.interfaces.LoggerInterface;

public class AbstractBasicController {
	
	@Autowired
	@Qualifier("logger-controller")
	protected LoggerInterface log;
	
	@ExceptionHandler( AbstractCustomHttpException.class )
	private ResponseEntity<String> handleException(AbstractCustomHttpException e, HttpServletRequest request ) {
		return new ResponseEntity<String>( e.getRequestMessage(request), e.getStatus() );
	}
}
