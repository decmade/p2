package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.VenueInterface;

@Entity
@Table(name = "venues")
public class Venue extends AbstractSportsRadarEntity implements VenueInterface {
	
	@Column(length = 50)
	private String name;
	
	@Column(length = 100)
	private String city;
	
	@Column(length = 2)
	private String state;
	
	@Column(length = 10)
	private String zip;
	
	@Column(length = 100)
	private String address;
	
	private int capacity;
	
	@Column(length = 50)
	private String surface;
	
	@Column(length = 50)
	private String roofType;

	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String getZip() {
		return zip;
	}

	@Override
	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String getSurface() {
		return surface;
	}

	@Override
	public void setSurface(String surface) {
		this.surface = surface;
	}

	@Override
	public String getRoofType() {
		return roofType;
	}

	@Override
	public void setRoofType(String roofType) {
		this.roofType = roofType;
	}	
	
}
