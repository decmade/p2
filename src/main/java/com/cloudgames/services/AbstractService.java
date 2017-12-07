package com.cloudgames.services;

import java.util.List;

import com.cloudgames.logger.LoggerInterface;
import com.cloudgames.logger.ServiceLogger;
import com.cloudgames.services.interfaces.ServiceInterface;

abstract public class AbstractService<T> implements ServiceInterface<T> {
	protected static LoggerInterface log = ServiceLogger.getInstance();
	
	@Override
	abstract public T fetchById(int id);
	
	@Override
	abstract public List<T> fetchAll();
	
	@Override
	abstract public T save(T entity);
	
	@Override
	abstract public void delete(T entity);

}
