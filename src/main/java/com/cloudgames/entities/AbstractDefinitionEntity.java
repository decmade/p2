package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.DefinitionEntityInterface;

/**
 * this represents all entities that are definition tables
 * that consist of an ID column and a Description column
 * 
 * so for entities like this, we only need to define a
 * class to specify any specific deviations from this 
 * schema and to declare the name of the table
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@MappedSuperclass
public class AbstractDefinitionEntity extends AbstractEntity implements DefinitionEntityInterface {
	
	@Column(length = 100, unique = true)
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
