package com.cloudgames.acl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cloudgames.acl.interfaces.AuthorizerInterface;
import com.cloudgames.acl.interfaces.PolicyInterface;
import com.cloudgames.acl.models.Request;

/**
 * << singleton >>
 * 
 * represents the logic for authorizing a
 * user to have a certain type of access to
 * a given object
 * 
 * @author john.w.brown.jr@gmail.com
 */

@Service("authorizer")
public class Authorizer extends AbstractAclObject implements AuthorizerInterface {
	
	private List<PolicyInterface> policies;			// collection of policies to consider
	
	public Authorizer() {
		this.initializePolicies();
	}

	/**
	 * add a policy to the collection of used policies
	 * 
	 * @param PolicyInterface policy
	 * 
	 * @return self
	 */
	@Override
	public Authorizer addPolicy(PolicyInterface policy)
	{
		this.policies.add( policy );
		policy.setAuthorizer(this);
		
		return this;
	}
	
	/**
	 * tests for authorization to a given resource
	 * per the data in the ACLRequest
	 * 
	 * @param Request request
	 * 
	 * @return boolean
	 */
	@Override
	public boolean authorize(Request request)
	{
		String logMessage;
		
		for(PolicyInterface policy: this.policies ) {
			/*
			 * if any policy explicitly denies access
			 * return false
			 */
			if ( policy.deny( request) ) {
				logMessage = String.format("access explicity DENIED for USER: %s to RESOURCE: %s", 
					request.user.getIdentity(),
					String.valueOf( request.resource )
				);
				
				log.info( logMessage );
						
				return false;
			}
			
			/*
			 * if a policy explicitly allows access, 
			 * return true
			 */
			if ( policy.allow( request ) ) {
				
				logMessage = String.format("[%s] access explicitly ALLOWED for User:[%s] to Resource:[%s]", 
					request.verb,
					request.user.getIdentity(),
					String.valueOf( request.resource )
				);
				
				log.info( logMessage );
				return true;
			}
		}
		
		/*
		 * return false if no policies explicitly allow access
		 */
		logMessage = String.format("[%s] access implicitly DENIED for User:[%s] to Resource:[%s]", 
			request.verb,
			request.user.getIdentity(),
			String.valueOf( request.resource )
		);
			
		log.info( logMessage );
		
		return false;
	}
	
	/**
	 * initialize the collection of PolicyInterface objects used to
	 * evaluate requests
	 * 
	 * each policy should test for it's applicable object
	 * 
	 * any policy that approves a request results in an overall approval
	 */
	private void initializePolicies()
	{
		this.policies = new ArrayList<>();
		
		/*
		 * polices used by the Authorizer to evaluate
		 * requests
		 */
		
		
		
		/*
		 * example policies from my ERS project
		 */
		/*
		this.policies.add( new SystemAdministratorPolicy() );
		this.policies.add( new FinanceManagerPolicy() );
		this.policies.add( new ReimbursementPolicy() );
		this.policies.add( new UserPolicy() );
		this.policies.add( new ReimbursementStatusPolicy() );
		this.policies.add( new ReimbursementTypePolicy() );
		this.policies.add( new UserRolePolicy() );
		this.policies.add( new ReceiptPolicy() ); 
		*/
	}
}
