package com.cloudgames.acl.interfaces;

import com.cloudgames.acl.models.Request;

/**
 * contract for all ACL policies
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public interface PolicyInterface {
	
	/**
	 * returns true if the policy allows access for
	 * the request passed
	 * 
	 * @param Request request
	 * 
	 * @return boolean
	 */
	public boolean allow(Request request);
	
	/**
	 * returns true if the policy denies access for
	 * the request passed
	 * 
	 * @param Request request
	 * 
	 * @return boolean
	 */
	public boolean deny(Request request);

	void setAuthorizer(AuthorizerInterface authorizer);
}
