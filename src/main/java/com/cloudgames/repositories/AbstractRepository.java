package com.cloudgames.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.*;

import com.cloudgames.entities.interfaces.EntityInterface;
import com.cloudgames.logger.interfaces.LoggerInterface;
import com.cloudgames.repositories.interfaces.RepositoryInterface;

abstract public class AbstractRepository<T> implements RepositoryInterface<T> {
	
	@Autowired
	@Qualifier("logger-repository")
	protected LoggerInterface log;
	
	
	@Override
	abstract public T fetchById(int id);
	
	@Override
	abstract public List<T> fetchAll();
	
	@Override
	abstract public T save(T entity);
	
	@Override
	abstract public void delete(T entity);
	
}
