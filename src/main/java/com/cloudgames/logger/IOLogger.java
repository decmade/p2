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
public class IOLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static IOLogger instance = new IOLogger();
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static IOLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private IOLogger()
	{
		super();
		
		this.logger = Logger.getLogger("io");
	}
	
}
