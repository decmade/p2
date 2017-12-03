package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.UserInterface;

@Entity
@Table(name="users")
public class User implements UserInterface {
	@Id
	private int id;
	
	/**
	 * username using ACL terminology
	 */
	private String identity;	
	
	/**
	 * password (encrypted)
	 */
	private String credential;
	
	/**
	 * password salt
	 */
	private String secret;
	
	private String lastName;
	private String firstName;
	
	@ManyToOne
	private UserStatus status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	

}
