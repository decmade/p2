package com.cloudgames.repositories.interfaces;

import java.util.List;

import com.cloudgames.entities.interfaces.EntityInterface;

public interface RepositoryInterface<T> {

	T fetchById(int id);

	List<T> fetchAll();

	T save(T entity);

	void delete(T entity);

}
