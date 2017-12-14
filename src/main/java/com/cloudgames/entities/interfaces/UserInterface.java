package com.cloudgames.entities.interfaces;

import com.cloudgames.entities.Account;
import com.cloudgames.entities.UserRole;
import com.cloudgames.entities.UserStatus;

public interface UserInterface extends EntityInterface {

	public String getIdentity();

	public void setIdentity(String identity);

	public String getCredential();

	public void setCredential(String credential);

	public String getSecret();

	public void setSecret(String secret);

	public String getLastName();

	public void setLastName(String lastName);

	public String getFirstName();

	public void setFirstName(String firstName);

	public UserStatus getStatus();

	public void setStatus(UserStatus status);
	
	public String getEmail();

	public void setEmail(String email);

	public String getAddress1();

	public void setAddress1(String address1);

	public String getAddress2();

	public void setAddress2(String address2);

	public String getCity();

	public void setCity(String city);

	public String getState();

	public void setState(String state);

	public String getZip();
	
	public void setZip(String zip);

	public String getDob();

	public void setDob(String dob);

	public String getPhone();

	public void setPhone(String phone);
	
	public UserRole getRole();

	public void setRole(UserRole role);
	
	public Account getAccount();

	public void setAccount(Account account);
}
