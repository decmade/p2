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
public class BeansLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static BeansLogger instance = new BeansLogger();
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static BeansLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private BeansLogger()
	{
		super();
		
		this.logger = Logger.getLogger("beans");
	}
	
}
