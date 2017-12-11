package com.cloudgames.acl.policies;

import com.cloudgames.acl.AbstractAclObject;
import com.cloudgames.acl.interfaces.AuthorizerInterface;
import com.cloudgames.acl.interfaces.PolicyInterface;
import com.cloudgames.acl.markers.FinanceManagerMarker;
import com.cloudgames.acl.markers.SystemAdministratorMarker;
import com.cloudgames.acl.models.Request;
import com.cloudgames.entities.interfaces.UserInterface;

/**
 * template for all ACL policy objects
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
abstract public class AbstractPolicy extends AbstractAclObject implements PolicyInterface 
{
	protected AuthorizerInterface authorizer;
	
	@Override
	public void setAuthorizer(AuthorizerInterface authorizer) {
		this.authorizer = authorizer;
	}
	
	/**
	 * default allow policy of extending classes
	 * 
	 * @param Request request
	 * 
	 * @return boolean
	 */
	@Override
	public boolean allow(Request request)
	{
		log.debug("default ACL allow policy used");
		return false;
	}
	
	/**
	 * default deny policy of extending classes
	 * 
	 * @param Request request
	 * 
	 * @return boolean
	 */
	@Override
	public boolean deny(Request request)
	{
		log.debug( String.format("default ACL DENY policy used for %s access TO %s FOR %s",
			request.verb,
			request.resource,
			request.user.getIdentity()
		));
		return false;
	}
	
	/**
	 * returns true if the user passed has
	 * Finance Manager access
	 * 
	 * @param UserInterface user
	 * 
	 * @return boolean
	 */
	protected boolean isFinanceManager(UserInterface user)
	{
		Request request = new Request(user, "any", FinanceManagerMarker.getInstance() );
		
		return authorizer.authorize(request);
	}
	
	/** 
	 * allow a FinanceManager access
	 * 
	 * @param Request request
	 * 
	 * @return boolean
	 */
	protected boolean userIsFinanceManager(Request request)
	{
		String logMessage;
		
		/*
		 * allow Finance Managers access
		 */
		if( this.isFinanceManager( request.user ) ) {
			logMessage = String.format("USER: %s is ALLOWED access due to having Finance Manager privileges", 
				request.user.getIdentity()
			);
			
			log.debug( logMessage );
			
			return true;
		} else {
			return false;
		}
	
	}
	
	/**
	 * returns true if the user passed has
	 * Finance Manager access
	 * 
	 * @param UserInterface user
	 * 
	 * @return boolean
	 */
	protected boolean isSystemAdministrator(UserInterface user)
	{
		Request request = new Request(user, "any", SystemAdministratorMarker.getInstance() );
		
		return this.authorizer.authorize(request);
	}
	
	/** 
	 * allow a SystemAdministrator access
	 * 
	 * @param Request request
	 * 
	 * @return boolean
	 */
	protected boolean userIsSystemAdministrator(Request request)
	{
		String logMessage;
		
		/*
		 * allow System Administrators access
		 */
		if( this.isSystemAdministrator( request.user ) ) {
			logMessage = String.format("USER: %s is ALLOWED access due to having System Administrator privileges", 
				request.user.getIdentity()
			);
			
			log.debug( logMessage );
			
			return true;
		} else {
			return false;
		}
	
	}
}
