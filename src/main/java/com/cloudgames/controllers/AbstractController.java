package com.cloudgames.controllers;

import java.util.List;


import com.cloudgames.controllers.interfaces.ControllerInterface;

abstract public class AbstractController<T,K> extends AbstractBasicController implements ControllerInterface<T,K> {
	
	
	@Override
	abstract public T get(int id);
	
	@Override
	abstract public List<T> getAll();
	
	@Override
	abstract public T save(K entity);
	
	@Override
	abstract public void remove(K entity);
	

	

}
