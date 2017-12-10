package com.cloudgames.acl.interfaces;

import com.cloudgames.acl.Authorizer;
import com.cloudgames.acl.models.Request;

public interface AuthorizerInterface {

	boolean authorize(Request request);

	Authorizer addPolicy(PolicyInterface policy);

}
