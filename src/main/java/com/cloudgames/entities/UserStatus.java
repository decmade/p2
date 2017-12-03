package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.UserStatusInterface;

@Entity
@Table(name="user_status")
public class UserStatus implements UserStatusInterface {
	@Id
	private int id;
	
	private String description;
}
