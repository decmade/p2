package com.cloudgames.logger;

import org.apache.log4j.Logger;

/**
 * << singleton >>
 * 
 * system-wide log
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class AclLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static AclLogger instance = new AclLogger();
	final private static String LOG_CATEGORY = "acl";
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static AclLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private AclLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
