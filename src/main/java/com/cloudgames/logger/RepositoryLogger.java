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
public class RepositoryLogger extends AbstractLog4jLogger
{
	/**
	 * singleton instantiation
	 */
	private static RepositoryLogger instance = new RepositoryLogger();
	
	final static private String LOG_CATEGORY = "respositories";
	
	/**
	 * provide singleton instance to clients
	 * 
	 * @return SysLog
	 */
	public static RepositoryLogger getInstance()
	{
		return instance;
	}
	
	/**
	 * hidden constructor
	 */
	private RepositoryLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
