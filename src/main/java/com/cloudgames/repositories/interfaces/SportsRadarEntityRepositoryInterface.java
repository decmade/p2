package com.cloudgames.repositories.interfaces;

public interface SportsRadarEntityRepositoryInterface<T> extends RepositoryInterface<T> {
	T fetchBySportsRadarId(String id);
}
