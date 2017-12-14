package com.cloudgames.entities;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.UserInterface;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


@Entity
@Table(name="users")
public class User extends AbstractEntity implements UserInterface {
		
	/**
	 * AKA: username
	 */
	@Column(unique = true)
	private String identity;	
	
	/**
	 * AKA: encrypted password (encrypted)
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private String credential;
	
	/**
	 * password salt
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private String secret;
	
	@Column(length=50)
	private String lastName;
	
	@Column(length=50)
	private String firstName;
	
	@Column(length=100)
	private String email;
	
	private String address1;
	
	private String address2;
	
	@Column(length=50)
	private String city;
	
	@Column(length=50)
	private String state;
	
	@Column(length=10)
	private String zip;
	
	private String dob;
	
	@Column(length=10)
	private String phone;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="account_id")
	private Account account;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="status_id")
	private UserStatus status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="role_id")
	private UserRole role;

	
	@Override
	public String getIdentity() {
		return this.identity;
	}

	@Override
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Override
	public String getCredential() {
		return credential;
	}

	@Override
	public void setCredential(String credential) {
		this.credential = credential;
	}

	@Override
	public String getSecret() {
		return secret;
	}

	@Override
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public UserStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getAddress1() {
		return address1;
	}

	@Override
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Override
	public String getAddress2() {
		return address2;
	}

	@Override
	public void setAddress2(String address2) {
		this.address2 = address2;
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
	public String getDob() {
		return dob;
	}

	@Override
	public void setDob(String dob) {
		this.dob = dob;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public UserRole getRole() {
		return role;
	}

	@Override
	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public Account getAccount() {
		return account;
	}

	@Override
	public void setAccount(Account account) {
		this.account = account;
	}
	

}
