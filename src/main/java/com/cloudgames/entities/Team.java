package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.TeamInterface;

@Entity
@Table(name = "teams")
public class Team extends AbstractSportsRadarEntity implements TeamInterface{

	@Column(length = 50)
	private String name;
	
	@Column(length = 50)
	private String market;
	
	@Column(length = 10)
	private String alias;
	
	@Column(length = 100)
	private String city;
	
	@Column(length = 50)
	private String state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venue_id")
	private Venue venue;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getMarket() {
		return market;
	}

	@Override
	public void setMarket(String market) {
		this.market = market;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public void setAlias(String alias) {
		this.alias = alias;
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
	public Venue getVenue() {
		return venue;
	}

	@Override
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
}
