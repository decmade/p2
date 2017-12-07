package com.cloudgames.repositories;

import java.util.List;

import com.cloudgames.logger.LoggerInterface;
import com.cloudgames.logger.RepositoryLogger;
import com.cloudgames.repositories.interfaces.RepositoryInterface;

abstract public class AbstractRepository<T> implements RepositoryInterface<T> {
	
	protected static LoggerInterface log = RepositoryLogger.getInstance();
	
	
	@Override
	abstract public T fetchById(int id);
	
	@Override
	abstract public List<T> fetchAll();
	
	@Override
	abstract public T save(T entity);
	
	@Override
	abstract public void delete(T entity);
	
}
