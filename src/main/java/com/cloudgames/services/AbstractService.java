package com.cloudgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.*;

import com.cloudgames.logger.interfaces.LoggerInterface;
import com.cloudgames.services.interfaces.ServiceInterface;

abstract public class AbstractService<T> implements ServiceInterface<T> {
	
	@Autowired
	@Qualifier("logger-service")
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
