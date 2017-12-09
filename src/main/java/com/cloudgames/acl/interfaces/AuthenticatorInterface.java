package com.cloudgames.acl.interfaces;

import javax.servlet.http.HttpSession;

import com.cloudgames.entities.interfaces.UserInterface;

public interface AuthenticatorInterface {

	boolean authenticate(String identity, String password);

	UserInterface getAuthenticatedUser();

	void clear();

	void setSession(HttpSession session);

}
