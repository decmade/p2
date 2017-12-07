package com.cloudgames.controllers.interfaces;

import java.util.List;

public interface ControllerInterface<T,K> {

	T get(int id);

	List<T> getAll();

	T save(K entity);

	void remove(K entity);

}
