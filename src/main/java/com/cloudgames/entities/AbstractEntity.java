package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.EntityInterface;

/**
 * this class will be the base entity class that all
 * entities can inherit from
 * 
 * the MappedSuperClass annotation tells Hibernate to get fields
 * from this class in addition to any fields defined in an entity 
 * class that extends it
 * 
 * this makes it easy to add fields to every entity at once if
 * we decide we want certain fields to be present on all records
 * later
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@MappedSuperclass
public class AbstractEntity implements EntityInterface {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
