package com.cloudgames.services;

import com.cloudgames.services.interfaces.SportsRadarEntityServiceInterface;

abstract public class AbstractSportsRadarEntityService<T> extends AbstractService<T> implements SportsRadarEntityServiceInterface<T> {
	
	@Override
	abstract public T fetchBySportsRadarId(String id);

}
