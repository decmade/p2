package com.cloudgames.services.interfaces;

public interface SportsRadarEntityServiceInterface<T> extends ServiceInterface<T> {
	T fetchBySportsRadarId(String id);
}
