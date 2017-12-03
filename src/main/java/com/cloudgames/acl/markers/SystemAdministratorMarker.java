package com.cloudgames.acl.markers;

import java.util.ArrayList;
import java.util.List;

/**
 * << singleton >>
 * 
 * used as a reference object for the FinanceManagerPolicy to
 * determine who has FinanceManagerAccess
 * 
 */
public class SystemAdministratorMarker
{
	/*
	 * instantiate singleton instance
	 */
	private static SystemAdministratorMarker instance = new SystemAdministratorMarker();
	
	/**
	 * returns singleton instance
	 * 
	 * @return FinanceManagerMarker
	 */
	public static SystemAdministratorMarker getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private SystemAdministratorMarker()
	{
		super();
	}
	
	/**
	 * returns list of user role descriptions that
	 * have Finance Manager access
	 * 
	 * @return List<String>
	 */
	public List<Integer> getRoleIds()
	{
		List<Integer> roles = new ArrayList<>();
		
		roles.add(1);
		
		/*
		 * the other way(s) are to use a many-to-many relationship
		 * between users and roles and/or allow roles to have 
		 * parent roles for nesting capability #beyondScope
		 */
		
		return roles;
	}
}
