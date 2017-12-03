package com.cloudgames.entities.wrappers;

import java.util.Map;

import com.cloudgames.entities.UserStatus;
import com.cloudgames.entities.interfaces.UserInterface;

public class UserWrapper extends AbstractEntityWrapper<UserInterface> implements UserInterface {

	
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdentity(String identity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCredential() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCredential(String credential) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSecret() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSecret(String secret) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLastName(String lastName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFirstName(String firstName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatus(UserStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getPropertyMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean checkCredential(String credential) {
		// TODO: implement this to check to see if the password passed matches the user's
		return false;
	}

	
}
