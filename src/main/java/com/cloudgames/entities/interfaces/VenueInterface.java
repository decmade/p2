package com.cloudgames.entities.interfaces;

public interface VenueInterface extends EntityInterface {

	String getName();

	void setName(String name);

	String getCity();

	void setCity(String city);

	String getState();

	void setState(String state);

	String getZip();

	void setZip(String zip);

	String getAddress();

	void setAddress(String address);

	int getCapacity();

	void setCapacity(int capacity);

	String getSurface();

	void setSurface(String surface);

	String getRoofType();

	void setRoofType(String roofType);

	String getSportRadarId();

	void setSportRadarId(String sportRadarId);
	
}