package com.cloudgames.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cloudgames.controllers.interfaces.ControllerInterface;
import com.cloudgames.exceptions.AbstractCustomHttpException;
import com.cloudgames.exceptions.CustomHttpExceptionInterface;
import com.cloudgames.logger.ControllerLogger;
import com.cloudgames.logger.LoggerInterface;

abstract public class AbstractController<T,K> implements ControllerInterface<T,K> {
	protected static LoggerInterface log = ControllerLogger.getInstance();
	
	@Override
	abstract public T get(int id);
	
	@Override
	abstract public List<T> getAll();
	
	@Override
	abstract public T save(K entity);
	
	@Override
	abstract public void remove(K entity);
	
	@ExceptionHandler( AbstractCustomHttpException.class )
	private ResponseEntity<String> handleException(AbstractCustomHttpException e, HttpServletRequest request ) {
		return new ResponseEntity<String>( e.getRequestMessage(request), e.getStatus() );
	}
	

}
