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
public class EntityLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static EntityLogger instance = new EntityLogger();
	final private static String LOG_CATEGORY = "entities";
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static EntityLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private EntityLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
