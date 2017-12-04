package com.cloudgames.entities.interfaces;

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
}
