package com.cloudgames.services.interfaces;

import java.util.List;

public interface ServiceInterface<T> {

	T fetchById(int id);

	List<T> fetchAll();

	T save(T entity);

	void delete(T entity);
	
}
