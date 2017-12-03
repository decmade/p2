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
public class FinanceManagerMarker
{
	/*
	 * instantiate singleton instance
	 */
	private static FinanceManagerMarker instance = new FinanceManagerMarker();
	
	/**
	 * returns singleton instance
	 * 
	 * @return FinanceManagerMarker
	 */
	public static FinanceManagerMarker getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private FinanceManagerMarker()
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
		
		roles.add(3);
		
		/*
		 * can be used to indicate a hierarchy without depending on
		 * surrogate keys for detection as below:
		 */
//		roles.add(4);	
		
		/*
		 * the other way(s) are to use a many-to-many relationship
		 * between users and roles and/or allow roles to have 
		 * parent roles for nesting capability #beyondScope
		 */
		
		return roles;
	}
}
