package com.cloudgames.services;

import com.cloudgames.repositories.interfaces.SportsRadarEntityRepositoryInterface;
import com.cloudgames.services.interfaces.SportsRadarEntityServiceInterface;

abstract public class AbstractSportsRadarEntityService<T> extends AbstractService<T> implements SportsRadarEntityServiceInterface<T> {
	
	abstract public T fetchBySportsRadarId(String id);

}
