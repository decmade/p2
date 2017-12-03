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
public class DbalLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static DbalLogger instance = new DbalLogger();
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static DbalLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private DbalLogger()
	{
		super();
		
		this.logger = Logger.getLogger("dbal");
	}
	
}
