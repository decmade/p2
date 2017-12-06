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
	
	private LocalDateTime dob;
	
	@Column(length=10)
	private String phone;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="account_id")
	private Account account;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="status_id")
	private UserStatus status;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="role_id")
	private UserRole role;

	
	
	
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public LocalDateTime getDob() {
		return dob;
	}

	public void setDob(LocalDateTime dob) {
		this.dob = dob;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	

}
