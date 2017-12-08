package com.cloudgames.entities.interfaces;

import com.cloudgames.entities.Venue;

public interface TeamInterface {

	String getName();

	void setName(String name);

	String getMarket();

	void setMarket(String market);

	String getAlias();

	void setAlias(String alias);

	String getCity();

	void setCity(String city);

	String getState();

	void setState(String state);

	Venue getVenue();

	void setVenue(Venue venue);

}
