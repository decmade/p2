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
public class ControllerLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static ControllerLogger instance = new ControllerLogger();
	final private static String LOG_CATEGORY = "controllers";
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static ControllerLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private ControllerLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
