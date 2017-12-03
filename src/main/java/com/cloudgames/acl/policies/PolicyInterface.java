package com.cloudgames.acl.policies;

import com.cloudgames.acl.Request;

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
}
