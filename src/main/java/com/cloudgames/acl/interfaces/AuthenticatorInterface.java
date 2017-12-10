package com.cloudgames.acl.interfaces;

import javax.servlet.http.HttpSession;

import com.cloudgames.acl.models.Credentials;
import com.cloudgames.entities.interfaces.UserInterface;

public interface AuthenticatorInterface {

	UserInterface getAuthenticatedUser();

	void clear();

	void setSession(HttpSession session);

	boolean authenticate(Credentials credentials);

}
