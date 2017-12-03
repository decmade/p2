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
public class SysLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static SysLogger instance = new SysLogger();
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static SysLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private SysLogger()
	{
		super();
		
		this.logger = Logger.getRootLogger();
	}
	
}
