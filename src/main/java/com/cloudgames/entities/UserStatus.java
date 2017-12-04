package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.UserStatusInterface;

/**
 * the properties of this class are already defined by the
 * AbstractDefinitionEntity
 * 
 * the reason for making this a class is so it can be instantiated
 * and refer to a particular table
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@Entity
@Table(name="user_status")
public class UserStatus extends AbstractDefinitionEntity implements UserStatusInterface {
	
}
