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
public class ServiceLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static ServiceLogger instance = new ServiceLogger();
	
	final static private String LOG_CATEGORY = "services";
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static ServiceLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private ServiceLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
