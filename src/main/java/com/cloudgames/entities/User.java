package com.cloudgames.entities;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.UserInterface;

@Entity
@Table(name="users")
public class User extends AbstractEntity implements UserInterface {
	
	/**
	 * AKA: username
	 */
	private String identity;	
	
	/**
	 * AKA: encrypted password (encrypted)
	 */
	private String credential;
	
	/**
	 * password salt
	 */
	private String secret;
	
	private String lastName;
	private String firstName;
	private String email;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private LocalDateTime dob;
	
	@ManyToOne
	private UserStatus status;

	public String getIdentity() {
		return this.identity;
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
