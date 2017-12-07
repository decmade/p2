package com.cloudgames.controllers;

import java.util.List;

import com.cloudgames.controllers.interfaces.ControllerInterface;
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
	

}
