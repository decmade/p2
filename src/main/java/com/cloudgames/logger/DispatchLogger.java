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
public class DispatchLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static DispatchLogger instance = new DispatchLogger();
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static DispatchLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private DispatchLogger()
	{
		super();
		
		this.logger = Logger.getLogger("dispatch");
	}
	
}
