package com.cloudgames.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.cloudgames.entities.interfaces.SportsRadarEntityInterface;

/**
 * this represents all entities that are retrieved from the 
 * SportsRadar API
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@MappedSuperclass
public class AbstractSportsRadarEntity extends AbstractEntity implements SportsRadarEntityInterface {
	
	@Column(length = 50)
	private String sportsRadarId;
	
	@Override
	public String getSportsRadarId() {
		return sportsRadarId;
	}
	
	@Override
	public void setSportsRadarId(String sportsRadarId) {
		this.sportsRadarId = sportsRadarId;
	}
}
